package com.genedu.lecturecontent.webclient;

import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.lecturecontent.dto.LessonEntityResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ContentWebClientService {
    private final WebClient contentWebClient;
    private static final String GET_LESSON_PLAN_FILE_URI = "/contents/lessons/{lessonId}/full-entity";

    public ContentWebClientService(
            @Qualifier("contentWebClient")
            WebClient contentWebClient
    ) {
        this.contentWebClient = contentWebClient;
    }

    public LessonEntityResponseDTO getLessonByLessonId(Long lessonId) {
        if (lessonId == null) {
            throw new IllegalArgumentException("Lesson ID must not be null");
        }

        return contentWebClient.get()
                .uri(GET_LESSON_PLAN_FILE_URI, lessonId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationUtils.extractJwt())
                .retrieve()
                .bodyToMono(LessonEntityResponseDTO.class)
                .block();
    }
}
