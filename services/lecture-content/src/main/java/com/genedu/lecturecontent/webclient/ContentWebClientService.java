package com.genedu.lecturecontent.webclient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ContentWebClientService {
//    private final WebClient contentWebClient;
//    private static final String GET_LESSON_BY_ID_URI = "/medias/projects/lesson-plans/{projectId}";
//
//    public ContentWebClientService(
//            @Qualifier("contentWebClient") WebClient.Builder contentWebClient
//    ) {
//        this.contentWebClient = contentWebClient.build();
//    }
//
//    public LessonPlanFileDownloadDTO getLessonPlanFileUrlByLessonPlanId(Long fileId) {
//        if (fileId == null) {
//            return null;
//        }
//        return contentWebClient.get()
//                .uri(GET_LESSON_BY_ID_URI, fileId)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationUtils.extractJwt())
//                .retrieve()
//                .bodyToMono(LessonPlanFileDownloadDTO.class)
//                .block();
//    }
}
