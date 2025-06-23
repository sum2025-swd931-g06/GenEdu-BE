package com.genedu.project.service;


import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.project.dto.client.LectureFileDownloadDTO;
import com.genedu.project.dto.client.LectureFileUploadDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
public class LectureMediaWebClientService {
    private final WebClient webClient;

    public LectureMediaWebClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String getLessonPlanFileUrlByLessonPlanId(Long fileId) {
        return webClient.get()
                .uri("/medias/projects/lesson-plans/{fileId}/url", fileId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationUtils.extractJwt())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public LectureFileDownloadDTO uploadLectureFile(LectureFileUploadDTO fileUploadDTO) {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("mediaFile", fileUploadDTO.getMediaFile().getResource(), MediaType.MULTIPART_FORM_DATA)
                .filename(Objects.requireNonNull(fileUploadDTO.getMediaFile().getOriginalFilename()))
                .contentType(MediaType.parseMediaType(Objects.requireNonNull(fileUploadDTO.getMediaFile().getContentType())));
        bodyBuilder.part("projectId", fileUploadDTO.getProjectId().toString());

        return webClient.post()
                .uri("/medias/projects/lesson-plans/upload")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + AuthenticationUtils.extractJwt())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(LectureFileDownloadDTO.class)
                .block();
    }
}

