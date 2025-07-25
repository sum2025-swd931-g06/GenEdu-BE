package com.genedu.lecturecontent.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.lecturecontent.dto.*;
import com.genedu.lecturecontent.dto.webclient.SlideContentResponseDTO;
import com.genedu.lecturecontent.service.SlideContentService;
import com.genedu.lecturecontent.webclient.ProjectWebClientService;
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

import java.util.*;
import java.util.stream.Collectors;
import com.genedu.lecturecontent.dto.webclient.LectureContentRequestDTO;


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
    private final ProjectWebClientService projectWebClientService;

    private final Logger log = org.slf4j.LoggerFactory.getLogger(SlideContentController.class);

    public SlideContentController(
            @Qualifier("pgVectorStore")  VectorStore vectorStore,
            @Qualifier("openAiChatClient") ChatClient openAiChatClient,
            SlideContentService slideContentService,
            ProjectWebClientService projectWebClientService
    ) {
        this.vectorStore = vectorStore;
        this.openAiChatClient = openAiChatClient;
        this.slideContentService = slideContentService;
        this.projectWebClientService = projectWebClientService;
    }

    @PostMapping(
            path = "/{projectId}/slide-content"
    )
    public Flux<ServerSentEvent<Slide>> generateSlideByProjectIdAndSlideContentRequest(
            @PathVariable String projectId
    ) {
        log.info("Structuring lesson plan from provided content.");

        List<Slide> generatedSlides = new ArrayList<>();
        String jwtToken = AuthenticationUtils.extractJwt();

        BeanOutputConverter<LessonPlan> lessonPlanConverter = new BeanOutputConverter<>(LessonPlan.class);
        String format = lessonPlanConverter.getFormat();
        // 1. Get the uploaded lesson plan content by project ID.
        String lessonPlanContent = slideContentService.getLessonPlanContentByProjectId(projectId);
        PromptTemplate promptTemplate = new PromptTemplate(planGenerationPromptResource);
        promptTemplate.add("unstructured_content", lessonPlanContent);
        promptTemplate.add("format", format);
        Message promptMessage = promptTemplate.createMessage();

        ProjectResponseDTO projectResponseDTO = slideContentService.getProjectByProjectId(UUID.fromString(projectId));
        if (projectResponseDTO == null) {
            log.error("Project with ID {} not found.", projectId);
            return Flux.error(new IllegalArgumentException("Project not found"));
        }
        log.info("Project found: {}", projectResponseDTO);
        LessonEntityResponseDTO lessonEntityResponseDTO = slideContentService.getLessonByLessonId(projectResponseDTO.lessonId());

        if (lessonEntityResponseDTO == null) {
            log.error("Lesson with ID {} not found.", projectResponseDTO.lessonId());
            return Flux.error(new IllegalArgumentException("Lesson not found"));
        }
        log.info("Lesson found: {}", lessonEntityResponseDTO);
        SlideContentRequestDTO slideContentRequest = new SlideContentRequestDTO(
                lessonEntityResponseDTO.lessonId(),
                lessonEntityResponseDTO.chapterId(),
                lessonEntityResponseDTO.subjectId(),
                lessonEntityResponseDTO.materialId(),
                lessonEntityResponseDTO.schoolClassId(),
                "",
                projectResponseDTO.customInstructions()
        );

        // 2. Generate a structured lesson plan from the unstructured content.
        Generation generation = Objects.requireNonNull(
                openAiChatClient.prompt()
                        .messages(promptMessage)
                        .call().chatResponse()).getResult();

        LessonPlan lessonPlan = lessonPlanConverter.convert(generation.getOutput().getText());

        // 3. Validate the lesson plan structure.
        assert lessonPlan != null;
        final java.util.concurrent.atomic.AtomicLong counter = new java.util.concurrent.atomic.AtomicLong();
        log.info("Lesson plan structured successfully. Total activities: {}", lessonPlan.activities().size());

        for (Activity activity : lessonPlan.activities()) {
            log.info("Activity: {}", activity.name());
            for (Instruction instruction : activity.instructions()) {
                log.info("Instruction: {} - {}", instruction.name(), instruction.content());
            }
        }

        if (lessonPlan.activities().isEmpty()) {
            log.warn("No activities found in the lesson plan. Returning empty Flux.");
            return Flux.empty();
        }
        // 4. Generate slides for each instruction in the lesson plan.
        return Flux.fromIterable(
                lessonPlan.activities().stream()
                        .flatMap(activity -> activity.instructions().stream())
                        .toList()
                )
                .concatMap(instruction ->
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
                .doOnNext(slide -> {
                    generatedSlides.add(slide.data());
                })
                .doOnComplete(
                        () -> projectWebClientService.postLectureContent(
                                new LectureContentRequestDTO(
                                        projectId,
                                        lessonPlan.title(),
                                        generatedSlides.stream()
                                                .map(slide -> mapToSlideContentResponseDTO(
                                                        slide,
                                                        generatedSlides.indexOf(slide) + 1
                                                ))
                                                .toList()
                                ),
                                jwtToken
                        )
                );
    }

    private com.genedu.lecturecontent.dto.webclient.SlideContentRequestDTO mapToSlideContentResponseDTO(
            Slide slide,
            Integer orderNumber
    ) {
        String slideTitle = slide.title();
        String slideType = slide.type();
        Map<String, Object> subPoints = new HashMap<>();
        if (slide.data() != null) {
            subPoints = convertSlideDataToMap(slide.data());
        }
        String narrationScript = slide.narrationScript();
        com.genedu.lecturecontent.dto.webclient.SlideContentRequestDTO slideContentRequestDTO = new com.genedu.lecturecontent.dto.webclient.SlideContentRequestDTO(
                "",
                slideTitle,
                slideType,
                orderNumber,
                subPoints,
                narrationScript
        );
        log.info("Mapped Slide to SlideContentRequestDTO: {}", slideContentRequestDTO);
        return slideContentRequestDTO;
    }

    private Map<String, Object> convertSlideDataToMap(Slide.SlideData data) {
        if (data instanceof Slide.WelcomeSlideData d) {
            return Map.of("subtitle", d.subtitle());
        } else if (data instanceof Slide.ContentSlideData d) {
            return Map.of("body", d.body());
        } else if (data instanceof Slide.ListSlideData d) {
            return Map.of("items", d.items());
        } else if (data instanceof Slide.CompareSlideData d) {
            return Map.of(
                    "left_header", d.left_header(),
                    "left_points", d.left_points(),
                    "right_header", d.right_header(),
                    "right_points", d.right_points()
            );
        } else if (data instanceof Slide.ThanksSlideData d) {
            return Map.of("message", d.message());
        }
        return Collections.emptyMap(); // Fallback for unknown types
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
                    "schoolClassId == \"%s\" && subjectId == \"%s\" && materialId == \"%s\" && lessonId == \"%s\" && chapterId == \"%s\"",
                    slideContentRequest.schoolClassId(), slideContentRequest.subjectId(), slideContentRequest.materialId(), slideContentRequest.lessonId(), slideContentRequest.chapterId()
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

            log.info("Found {} similar documents for instruction '{}'. Context: {}", similarDocuments.size(), instruction.name(), context);

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
                            //.advisors(new MessageChatMemoryAdvisor(openAiChatMemory))
                            .call().chatResponse()).getResult();
            log.info("AI raw response: {}", generation.getOutput().getText());
            Slide slide = outputConverter.convert(generation.getOutput().getText());
            log.info("Finished generation for: {}. Slide: {}", instruction.name(), slide);
            return slide;
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
