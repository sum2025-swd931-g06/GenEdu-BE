package com.genedu.lecturecontent.webclient;

import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.lecturecontent.dto.ProjectResponseDTO;
import com.genedu.lecturecontent.dto.webclient.LectureContentRequestDTO;
import com.genedu.lecturecontent.dto.webclient.LectureContentResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
public class ProjectWebClientService {
    private final WebClient contentWebClient;
    private static final String GET_PROJECT_BY_ID_URI = "/projects/{projectId}";
    private static final String POST_LECTURE_CONTENT_URI = "/projects/{projectId}/lecture-content";

    public ProjectWebClientService(
            @Qualifier("projectWebClient")
            WebClient contentWebClient
    ) {
        this.contentWebClient = contentWebClient;
    }

    public ProjectResponseDTO getProjectByProjectID(UUID projectId) {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID must not be null");
        }

        return contentWebClient.get()
                .uri(GET_PROJECT_BY_ID_URI, projectId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationUtils.extractJwt())
                .retrieve()
                .bodyToMono(ProjectResponseDTO.class)
                .block();
    }

    public LectureContentResponseDTO postLectureContent(
            LectureContentRequestDTO lectureContentRequestDTO
    ) {
        if (lectureContentRequestDTO == null) {
            throw new IllegalArgumentException("Lecture content request DTO must not be null");
        }

        return contentWebClient.post()
                .uri(POST_LECTURE_CONTENT_URI, lectureContentRequestDTO.projectId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationUtils.extractJwt())
                .bodyValue(lectureContentRequestDTO)
                .retrieve()
                .bodyToMono(LectureContentResponseDTO.class)
                .block();
    }

}
