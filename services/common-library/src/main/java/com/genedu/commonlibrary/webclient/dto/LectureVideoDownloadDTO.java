package com.genedu.commonlibrary.webclient.dto;

import com.genedu.commonlibrary.enumeration.FileType;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Validated
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LectureVideoDownloadDTO {
    private Long id;
    private String fileName;
    private FileType fileType;
    private String fileUrl;
    private UUID uploadedBy;
    private LocalDateTime uploadedOn;
}
