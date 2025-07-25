package com.genedu.lecturecontent.webclient;

import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.dto.error.ErrorDTO;
import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.lecturecontent.dto.ProjectResponseDTO;
import com.genedu.lecturecontent.dto.webclient.LectureContentRequestDTO;
import com.genedu.lecturecontent.dto.webclient.LectureContentResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ProjectWebClientService {
    private final WebClient contentWebClient;
    private static final String GET_PROJECT_BY_ID_URI = "/projects/{projectId}";
    private static final String POST_LECTURE_CONTENT_URI = "/projects/lecture-contents";
    private static final String PUT_SLIDE_CONTENT_NARRATION_URI = "/projects/lecture-content/slide-content/{slideContentId}/narration-audio";

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
            LectureContentRequestDTO lectureContentRequestDTO,
            String jwtToken
    ) {
        if (lectureContentRequestDTO == null) {
            throw new IllegalArgumentException("Lecture content request DTO must not be null");
        }

        return contentWebClient.post()
                .uri(POST_LECTURE_CONTENT_URI)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .bodyValue(lectureContentRequestDTO)
                .retrieve()
                .bodyToMono(LectureContentResponseDTO.class)
                .block();
    }

    public void updateSlideContentNarration(
            UUID slideContentId,
            Long audioFileId,
            String jwtToken
    ) {
        // This validation is good practice.
        if (slideContentId == null || audioFileId == null) {
            throw new IllegalArgumentException("Slide content ID and audio file ID must not be null");
        }

        // Assuming PUT_SLIDE_CONTENT_NARRATION_URI is a constant like "/lecture-content/slide-content/{slideContentId}/narration-audio"
        contentWebClient.put()
                // CRITICAL FIX: Use a UriBuilder to correctly construct the URI with the query parameter.
                .uri(uriBuilder -> uriBuilder
                        .path(PUT_SLIDE_CONTENT_NARRATION_URI)
                        .queryParam("audioFileId", audioFileId)
                        .build(slideContentId)) // Provide the value for the {slideContentId} path variable
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .retrieve()
                // BEST PRACTICE: Add explicit error handling for remote calls to provide meaningful exceptions.
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(ErrorDTO.class) // Assuming an ErrorDTO exists
                                .flatMap(errorDto -> Mono.error(new BadRequestException(errorDto.title(), errorDto.detail())))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        clientResponse -> clientResponse.bodyToMono(ErrorDTO.class)
                                .flatMap(errorDto -> Mono.error(new RuntimeException("Project service failed: " + errorDto.detail())))
                )
                .bodyToMono(Void.class)
                .block(); // .block() is acceptable here if the calling method is not part of a reactive stream.

    }
}
