package com.genedu.project.webclient;


import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.dto.error.ErrorDTO;
import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.commonlibrary.webclient.dto.LessonPlanFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.LessonPlanFileUploadDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class LectureMediaWebClientService {

    @Qualifier("mediaWebClient")
    private final WebClient webClient;
    private static final String GET_LESSON_PLAN_FILE_URI = "/medias/projects/lesson-plans/{fileId}/url";
    private static final String UPLOAD_LESSON_PLAN_FILE_URI = "/medias/projects/lesson-plans/upload";
    private static final String GET_LESSON_PLAN_FILE_TEMPLATE_URI = "/medias/projects/lesson-plans/template";
    private static final String GET_SLIDE_NARRATION_AUDIO_FILE_URI = "/medias/projects/slides/narration-audios/{fileId}/url";

    public LectureMediaWebClientService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public String getSlideNarrationAudioFileUrlByFileId(Long fileId) {
        if (fileId == null) {
            return null;
        }
        return webClient.get()
                .uri(GET_SLIDE_NARRATION_AUDIO_FILE_URI, fileId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationUtils.extractJwt())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String getLessonPlanFileUrlByLessonPlanId(Long fileId) {
        if (fileId == null) {
            return null;
        }
        return webClient.get()
                .uri(GET_LESSON_PLAN_FILE_URI, fileId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationUtils.extractJwt())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public LessonPlanFileDownloadDTO uploadLectureFile(LessonPlanFileUploadDTO fileUploadDTO) {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("mediaFile", fileUploadDTO.getMediaFile().getResource(), MediaType.MULTIPART_FORM_DATA)
                .filename(Objects.requireNonNull(fileUploadDTO.getMediaFile().getOriginalFilename()))
                .contentType(MediaType.parseMediaType(Objects.requireNonNull(fileUploadDTO.getMediaFile().getContentType())));
        bodyBuilder.part("projectId", fileUploadDTO.getProjectId().toString());

        return webClient.post()
                .uri(UPLOAD_LESSON_PLAN_FILE_URI)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationUtils.extractJwt())
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError, // Target 4xx errors
                        clientResponse -> clientResponse.bodyToMono(ErrorDTO.class) // Deserialize the error body
                                .flatMap(errorDto -> Mono.error(new BadRequestException(errorDto.title(), errorDto.detail())))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError, // Target 5xx errors
                        clientResponse -> clientResponse.bodyToMono(ErrorDTO.class)
                                .flatMap(errorDto -> Mono.error(new RuntimeException(errorDto.title() + ": " + errorDto.detail())))
                )
                .bodyToMono(LessonPlanFileDownloadDTO.class)
                .block();
    }

    public LessonPlanFileDownloadDTO getLessonPlanTemplate() {
        return webClient.get()
                .uri(GET_LESSON_PLAN_FILE_TEMPLATE_URI)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationUtils.extractJwt())
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(ErrorDTO.class)
                                .flatMap(errorDto -> Mono.error(new BadRequestException(errorDto.title(), errorDto.detail())))
                )
                .bodyToMono(LessonPlanFileDownloadDTO.class)
                .block();
    }
}

