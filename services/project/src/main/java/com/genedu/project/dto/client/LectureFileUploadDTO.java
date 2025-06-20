package com.genedu.project.dto.client;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@Setter
@Validated
@RequiredArgsConstructor
@AllArgsConstructor
public class LectureFileUploadDTO
{
    @NotNull(message = "Project ID cannot be null")
    private UUID projectId;

    @NotNull(message = "Media file cannot be null")
    private MultipartFile mediaFile;
}
