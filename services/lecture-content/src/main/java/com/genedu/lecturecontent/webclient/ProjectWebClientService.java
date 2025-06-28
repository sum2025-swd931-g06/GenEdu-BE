package com.genedu.lecturecontent.webclient;

import com.genedu.lecturecontent.dto.ProjectResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ProjectWebClientService {
//    private final WebClient.Builder projectWebClient;
//    private final WebClient.Builder mediaWebClient;
//
//    public ProjectWebClientService(
//            @Qualifier("projectWebClient")
//            WebClient.Builder projectWebClient,
//            @Qualifier("lectureMediaWebClient")
//            WebClient.Builder mediaWebClient
//    ) {
//        this.projectWebClient = projectWebClient;
//        this.mediaWebClient = mediaWebClient;
//    }
//
//    public Mono<ProjectResponseDTO> getProjectById(UUID projectId) {
//        return projectWebClient.build()
//                .get()
//                .uri("/projects/{projectId}", projectId)
//                .retrieve()
//                .bodyToMono(ProjectResponseDTO.class);
//    }
//
//    public Mono<LessonPlanFileDownloadDTO> getLessonPlanContent(Long lessonPlanFileId) {
//        return mediaWebClient.build()
//                .get()
//                .uri("/lesson-plan-files/{fileId}/download", lessonPlanFileId)
//                .retrieve()
//                .bodyToMono(LessonPlanFileDownloadDTO.class);
//    }
}
