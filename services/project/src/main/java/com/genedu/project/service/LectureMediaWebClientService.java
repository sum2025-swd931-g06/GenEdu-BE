package com.genedu.project.service;


import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.project.dto.client.LectureFileDownloadDTO;
import com.genedu.project.dto.client.LectureFileUploadDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
public class LectureMediaWebClientService {

    @Qualifier("lectureMediaWebClient")
    private final WebClient webClient;
    private static final String GET_LESSON_PLAN_FILE_URI = "/medias/projects/lesson-plans/{fileId}/url";
    private static final String UPLOAD_LESSON_PLAN_FILE_URI = "/medias/projects/lesson-plans/upload";


    public LectureMediaWebClientService(WebClient.Builder builder) {
        this.webClient = builder.build();
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

    public LectureFileDownloadDTO uploadLectureFile(LectureFileUploadDTO fileUploadDTO) {
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
                .bodyToMono(LectureFileDownloadDTO.class)
                .block();
    }
}

