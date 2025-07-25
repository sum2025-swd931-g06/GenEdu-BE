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
public class SlideNarrationAudioUploadDTO {
    @NotNull(message = "Project ID cannot be null")
    private String projectId;
    @NotNull(message = "LectureContent ID cannot be null")
    private String lectureContentId;
    @NotNull(message = "Slide ID cannot be null")
    private String slideId;
    @NotNull(message = "Audio file cannot be null")
    private byte[] audioFile;
}
