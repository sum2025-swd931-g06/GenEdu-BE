package com.genedu.lecturecontent.webclient;

import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.commonlibrary.webclient.dto.LessonPlanFileDownloadDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class LectureMediaWebClientService {
    private final WebClient lectureMediaWebClient;

    public LectureMediaWebClientService(
            @Qualifier("lectureMediaWebClient")
            WebClient lectureMediaWebClient
    ) {
        this.lectureMediaWebClient = lectureMediaWebClient;
    }

    public LessonPlanFileDownloadDTO getLessonPlanFileContentByProjectId(String projectId) {
        if (projectId == null || projectId.isEmpty()) {
            throw new IllegalArgumentException("Project ID must not be null or empty");
        }

        return lectureMediaWebClient.get()
                .uri("/medias/projects/lesson-plans/project/{projectId}", projectId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationUtils.extractJwt())
                .retrieve()
                .bodyToMono(LessonPlanFileDownloadDTO.class)
                .block();
    }
}

