package com.genedu.commonlibrary.webclient.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Validated
@RequiredArgsConstructor
@AllArgsConstructor
public class LessonPlanFileUploadDTO
{
    @NotNull(message = "Project ID cannot be null")
    private String projectId;

    @NotNull(message = "Media file cannot be null")
    private MultipartFile mediaFile;
}
