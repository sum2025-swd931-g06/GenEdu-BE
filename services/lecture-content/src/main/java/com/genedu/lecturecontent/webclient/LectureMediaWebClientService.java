package com.genedu.lecturecontent.webclient;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class LectureMediaWebClientService {
//    private final WebClient lectureMediaWebClient;
//    private static final String GET_LESSON_PLAN_FILE_URI = "/medias/projects/lesson-plans/{fileId}";
//
//    public LectureMediaWebClientService(
//            @Qualifier("lectureMediaWebClient") WebClient.Builder lectureMediaWebClientBuilder
//    ) {
//        this.lectureMediaWebClient = lectureMediaWebClientBuilder.build();
//    }
//
//    public LessonPlanFileDownloadDTO getLessonPlanFileUrlByLessonPlanId(Long fileId) {
//        if (fileId == null) {
//            return null;
//        }
//        return lectureMediaWebClient.get()
//                .uri(GET_LESSON_PLAN_FILE_URI, fileId)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationUtils.extractJwt())
//                .retrieve()
//                .bodyToMono(LessonPlanFileDownloadDTO.class)
//                .block();
//    }
}

