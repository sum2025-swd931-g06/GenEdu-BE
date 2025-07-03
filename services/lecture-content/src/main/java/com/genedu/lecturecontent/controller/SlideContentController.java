package com.genedu.lecturecontent.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.genedu.lecturecontent.dto.Instruction;
import com.genedu.lecturecontent.dto.LessonPlan;
import com.genedu.lecturecontent.dto.Slide;
import com.genedu.lecturecontent.dto.SlideContentRequestDTO;
import com.genedu.lecturecontent.service.LessonPlanService;
import com.genedu.lecturecontent.service.SlideContentService;
import org.slf4j.Logger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/lecture-contents")
public class SlideContentController {
    @Value("classpath:prompts/sys-slide-content-template.st")
    private Resource systemPromptTemplate;

    @Value("classpath:prompts/sys-plan-generation-template.st")
    private Resource planGenerationPromptResource;

    @Value("classpath:prompts/sys-slide-generation-template.st")
    private Resource slideGenerationPromptResource;

    private final VectorStore vectorStore;
    private final ChatClient openAiChatClient;
    private final SlideContentService slideContentService;

    private final Logger log = org.slf4j.LoggerFactory.getLogger(SlideContentController.class);

    public SlideContentController(
            @Qualifier("pgVectorStore")  VectorStore vectorStore,
            @Qualifier("openAiChatClient") ChatClient openAiChatClient,
            SlideContentService slideContentService
    ) {
        this.vectorStore = vectorStore;
        this.openAiChatClient = openAiChatClient;
        this.slideContentService = slideContentService;
    }

    @PostMapping(
            path = "/{projectId}/slide-content",
            produces = "application/json;charset=UTF-8",
            consumes = "application/json;charset=UTF-8"
    )
    public Flux<ServerSentEvent<Slide>> generateSlideByProjectIdAndSlideContentRequest(
            @PathVariable String projectId,
            @RequestBody SlideContentRequestDTO slideContentRequest
    ) {
        log.info("Structuring lesson plan from provided content.");
        BeanOutputConverter<LessonPlan> lessonPlanConverter = new BeanOutputConverter<>(LessonPlan.class);
        String format = lessonPlanConverter.getFormat();

        String lessonPlanContent = slideContentService.getLessonPlanContentByProjectId(projectId);
        PromptTemplate promptTemplate = new PromptTemplate(planGenerationPromptResource);
        promptTemplate.add("unstructured_content", lessonPlanContent);
        promptTemplate.add("format", format);
        Message promptMessage = promptTemplate.createMessage();

        Generation generation = Objects.requireNonNull(
                openAiChatClient.prompt()
                        .messages(promptMessage)
                        .call().chatResponse()).getResult();

        LessonPlan lessonPlan = lessonPlanConverter.convert(generation.getOutput().getText());

        // 4. Generate slides for each instruction in the lesson plan.
        assert lessonPlan != null;
        final java.util.concurrent.atomic.AtomicLong counter = new java.util.concurrent.atomic.AtomicLong();

        return Flux.fromIterable(
                lessonPlan.activities().stream()
                        .flatMap(activity -> activity.instructions().stream())
                        .toList()
                )
                .flatMap(instruction ->
                        {
                            // This try-catch is not ideal for reactive code.
                            // A better approach is to use .onErrorResume in the chain.
                            try {
                                log.info("Generating slide for instruction: {} : {}", instruction.name(), instruction.content());
                                return generateSlideForInstruction(instruction, slideContentRequest);
                            } catch (JsonMappingException e) {
                                log.warn("Skipping slide due to JSON mapping error for instruction: {}", instruction.name(), e);
                                return Mono.empty();
                            }
                        }
                )
                // This is the key change: transform each Slide into a ServerSentEvent.
                .map(slide -> ServerSentEvent.<Slide>builder()
                        .id(String.valueOf(counter.incrementAndGet()))
                        .event("slide-generated") // A descriptive event name for the client
                        .data(slide)
                        .build()
                )
                .doOnComplete(() -> log.info("All slides generated successfully."));
    }

    private Mono<Slide> generateSlideForInstruction (
            Instruction instruction,
            SlideContentRequestDTO slideContentRequest
    ) throws JsonMappingException
    {
        log.info("Scheduling generation for instruction: '{}'", instruction.name());

        return Mono.fromCallable(() -> {
            log.info("Executing RAG + Generation for '{}' on a separate thread.", instruction.name());

            String searchQuery = instruction.name() + " - " + instruction.content();
            String requestFilter = String.format(
                    "schoolClassId == \"%s\" && subjectId == \"%s\" && materialId == \"%s\" && lessonId == \"%s\" && chapterId == \"%s\" && lessonContentId == \"%s\"",
                    slideContentRequest.schoolClassId(), slideContentRequest.subjectId(), slideContentRequest.materialId(), slideContentRequest.lessonId(), slideContentRequest.lessonContentId(), slideContentRequest.chapterId()
            );

            List<Document> similarDocuments = vectorStore.similaritySearch(
                    SearchRequest.builder()
                            .query(searchQuery)
                            .topK(5)
                            .filterExpression(requestFilter)
                            .build()
            );

            String context = similarDocuments.stream()
                    .map(Document::getFormattedContent)
                    .collect(Collectors.joining("\n\n"));

            BeanOutputConverter<Slide> outputConverter = new BeanOutputConverter<>(Slide.class);
            PromptTemplate promptTemplate = new PromptTemplate(slideGenerationPromptResource);
            Message promptMessage = promptTemplate.createMessage(
                    Map.of(
                            "instruction", instruction.name() + ": " + instruction.content(),
                            "context", context,
                            "format", outputConverter.getFormat()
                    )
            );

            Generation generation = Objects.requireNonNull(
                    openAiChatClient.prompt()
                            .messages(promptMessage)
                            .call().chatResponse()).getResult();
            log.info("AI raw response: {}", generation.getOutput().getText());
            Slide slide = outputConverter.convert(generation.getOutput().getText());
            log.info("Finished generation for: {}. Slide: {}", instruction.name(), slide);
            return slide;
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
