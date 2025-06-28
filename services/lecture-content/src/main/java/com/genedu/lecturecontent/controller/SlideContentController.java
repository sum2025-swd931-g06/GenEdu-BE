package com.genedu.lecturecontent.controller;

import com.genedu.lecturecontent.webclient.ProjectWebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/slide-contents")
@RequiredArgsConstructor
public class SlideContentController {
    private final OllamaChatModel chatModel;
    private final VectorStore vectorStore;
    private final Logger log = Logger.getLogger(SlideContentController.class.getName());

    @Value("classpath:prompts/sys-slide-content-template.st")
    private Resource systemPromptTemplate;

//    @PostMapping(
//        value = "/projects/{projectId}/stream",
//        produces = "application/json;charset=UTF-8",
//        consumes = "application/json;charset=UTF-8"
//    )public Flux<String> streamSlideContentByProjectId(
//            @PathVariable UUID projectId
//    ) {
//
//    }

    @PostMapping(
        value = "/stream",
        produces = "application/json;charset=UTF-8",
        consumes = "application/json;charset=UTF-8"
    )
    public Flux<String> streamSlideContent(
            @RequestBody String prompt
    ) {
        record SlideContentRequest(
            String lessonId,
            String chapterId,
            String subjectId,
            String materialId,
            String schoolClassId,
            String lessonContentId
        ) {}

        List<SlideContentRequest> requests = List.of(
            new SlideContentRequest("2", "1", "1", "1", "1", "1"),
            new SlideContentRequest("2", "1", "1", "1", "1", "2")
        );

        PromptTemplate promptTemplate = new PromptTemplate(systemPromptTemplate);

        List<QuestionAnswerAdvisor> advisors = new ArrayList<>();
        List<Document> documents = new ArrayList<>();

        String query = "Các phong trào tiêu biểu của Phong trào yêu nước chống Pháp cuối thế kỷ XIX là gì?";

        for (SlideContentRequest request : requests) {
            String requestFilter = String.format(
                    "schoolClassId == \"%s\" && subjectId == \"%s\" && materialId == \"%s\" && lessonId == \"%s\" && chapterId == \"%s\" && lessonContentId == \"%s\"",
                    request.schoolClassId, request.subjectId, request.materialId, request.lessonId, request.chapterId, request.lessonContentId
            );
            log.info("Adding advisor for filter: " + requestFilter);
            QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(vectorStore)
                    .searchRequest(
                            SearchRequest.builder()
                                    .query(query)
                                    .filterExpression(requestFilter)
                                    .build()
                    ).build();
            advisors.add(advisor);
            List<Document> docs = vectorStore.similaritySearch(
                    SearchRequest.builder()
                            .query(query)
                            .topK(3)
                            .filterExpression(requestFilter)
                            .build()
            );
            if (docs != null && !docs.isEmpty()) {
                documents.addAll(docs);
            } else {
                log.warning("No documents found for filter: " + requestFilter);
            }
        }

        for (Document doc : documents) {
            log.info("Found document: " + doc.getFormattedContent());
            log.info("Metadata: " + doc.getMetadata());
        }

        promptTemplate.add("question", prompt);
        Prompt chatPrompt = promptTemplate.create();

        return ChatClient.builder(chatModel)
                .build()
                .prompt()
                .user(prompt)
                .advisors(
                        advisors.toArray(new QuestionAnswerAdvisor[0])
                )
                .system(chatPrompt.getContents())
                .stream()
                .content();
    }
}
