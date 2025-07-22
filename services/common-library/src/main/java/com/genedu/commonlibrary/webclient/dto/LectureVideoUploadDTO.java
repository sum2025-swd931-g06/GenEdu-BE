package com.genedu.commonlibrary.webclient.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@AllArgsConstructor
@NoArgsConstructor
public class LectureVideoUploadDTO {
    @NotNull(message = "Project ID cannot be null")
    private String projectId;
    @NotNull(message = "LectureContent ID cannot be null")
    private String lectureContentId;
    @NotNull(message = "FinalizedLecture ID cannot be null")
    private String finalizeLectureId;
    @NotNull(message = "Video file cannot be null")
    private byte[] videoFile;
}
