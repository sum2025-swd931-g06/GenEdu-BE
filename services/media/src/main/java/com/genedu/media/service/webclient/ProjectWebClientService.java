package com.genedu.media.service.webclient;

import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.dto.error.ErrorDTO;
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
    private static final String PUT_LECTURE_CONTENT_URI = "/projects/finalized-lectures/{finalizedLectureId}/lecture-videos";

    public ProjectWebClientService(
            @Qualifier("projectWebClient")
            WebClient contentWebClient
    ) {
        this.contentWebClient = contentWebClient;
    }

    public void updateFinalizedLectureVideo(
            UUID finalizedLectureId,
            Long lectureVideoId,
            String jwtToken
    ) {
        // This validation is good practice.
        if (finalizedLectureId == null || lectureVideoId == null) {
            throw new IllegalArgumentException("Finalized lecture ID and lecture video ID must not be null.");
        }

        // Assuming PUT_SLIDE_CONTENT_NARRATION_URI is a constant like "/lecture-content/slide-content/{slideContentId}/narration-audio"
        contentWebClient.put()
                // CRITICAL FIX: Use a UriBuilder to correctly construct the URI with the query parameter.
                .uri(uriBuilder -> uriBuilder
                        .path(PUT_LECTURE_CONTENT_URI)
                        .queryParam("lectureVideoId", lectureVideoId)
                        .build(finalizedLectureId)) // Provide the value for the {slideContentId} path variable
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
