package com.genedu.lecturecontent.webclient;

import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.dto.error.ErrorDTO;
import com.genedu.commonlibrary.webclient.dto.LessonPlanFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.SlideNarrationAudioDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.SlideNarrationAudioUploadDTO;
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
public class SlideNarrationMediaWebClientService {
    private final WebClient webClient;

    private static final String UPLOAD_SLIDE_NARRATION_AUDIO_URL = "/medias/projects/slides/narration-audios/upload";

    public SlideNarrationMediaWebClientService(
            @Qualifier("lectureMediaWebClient")
            WebClient lectureMediaWebClient)
    {
        this.webClient = lectureMediaWebClient;
    }

    public SlideNarrationAudioDownloadDTO uploadNarrationFile(SlideNarrationAudioUploadDTO fileUploadDTO, String jwt) {
        return webClient.post()
                .uri(UPLOAD_SLIDE_NARRATION_AUDIO_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .body(BodyInserters.fromValue(fileUploadDTO))
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
                .bodyToMono(SlideNarrationAudioDownloadDTO.class)
                .block();
    }
}

