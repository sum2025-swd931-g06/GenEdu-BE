package com.genedu.project.webclient;

import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.dto.error.ErrorDTO;
import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.commonlibrary.webclient.dto.SlideFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.SlideFileUploadDTO;
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
import java.util.UUID;

@Service
public class ProjectMediaWebClientService {
    @Qualifier("mediaWebClient")
    private final WebClient webClient;

    private static final String UPLOAD_LESSON_SLIDE_FILE_URI = "/medias/projects/slides/upload";
    private static final String GET_SLIDE_FILE_URL = "/medias/projects/slides/{fileId}/url";


    public ProjectMediaWebClientService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public String getGetSlideFileUrl(Long fileId) {
        if (fileId == null) {
            return null;
        }
        return webClient.get()
                .uri(GET_SLIDE_FILE_URL, fileId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationUtils.extractJwt())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public SlideFileDownloadDTO uploadSlideFile(SlideFileUploadDTO fileUploadDTO) {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("mediaFile", fileUploadDTO.getMediaFile().getResource(), MediaType.MULTIPART_FORM_DATA)
                .filename(Objects.requireNonNull(fileUploadDTO.getMediaFile().getOriginalFilename()))
                .contentType(MediaType.parseMediaType(Objects.requireNonNull(fileUploadDTO.getMediaFile().getContentType())));
        bodyBuilder.part("projectId", fileUploadDTO.getProjectId());
        bodyBuilder.part("lectureContentId", fileUploadDTO.getLectureContentId());

        return webClient.post()
                .uri(UPLOAD_LESSON_SLIDE_FILE_URI)
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
                .bodyToMono(SlideFileDownloadDTO.class)
                .block();
    }

    public String getAudioFileUrl(Long audioFileId) {
        // Logic to get audio file URL
        return "https://example.com/audio/" + audioFileId;
    }
    public String getPresentationFileUrl(Long presentationFileId) {
        return this.getGetSlideFileUrl(presentationFileId);
    }
    public String getVideoFileUrl(Long videoFileId) {
        if (videoFileId == null) {
            return null;
        }
        return webClient.get()
                .uri(GET_SLIDE_FILE_URL, videoFileId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationUtils.extractJwt())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    public String getThumbnailFileUrl(Long thumbnailFileId) {
        // Logic to get thumbnail file URL
        return "https://example.com/thumbnail/" + thumbnailFileId;
    }
}
