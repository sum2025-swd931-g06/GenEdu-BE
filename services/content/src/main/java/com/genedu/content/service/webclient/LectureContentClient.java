package com.genedu.content.service.webclient;


import com.genedu.commonlibrary.enumeration.LessonStatus;
import com.genedu.content.dto.client.LectureContentRequestDTO;
import com.genedu.content.model.Lesson;
import com.genedu.content.service.LessonContentService;
import com.genedu.content.service.LessonService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class LectureContentClient {
    private final WebClient webClient;
    private final LessonContentService lessonContentService;
    private final LessonService lessonService;

    public LectureContentClient(WebClient.Builder builder, LessonContentService lessonContentService, LessonService lessonService) {
        this.webClient = builder
                .baseUrl("http://lecture-content-service/api/v1/lecture-contents")
                .build();
        this.lessonContentService = lessonContentService;
        this.lessonService = lessonService;
    }

    public void createVectorEmbeddings (Long lessonId) {
        Lesson lesson = lessonService.getLessonEntityById(lessonId);
        if (lesson.isDeleted()){
            throw new IllegalStateException("Lesson is deleted!");
        }else if (lesson.getStatus().equals(LessonStatus.SYNCED.toString())){
            throw new IllegalStateException("Lesson is already synced!");
        }

        List<LectureContentRequestDTO> lectureContents = lessonContentService.getLectureContentsByLessonId(lessonId);
        webClient.post()
                .uri("/embeddings")
                .acceptCharset(StandardCharsets.UTF_8)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(lectureContents)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
