package com.genedu.commonlibrary.webclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.genedu.commonlibrary.enumeration.FileType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class LessonPlanFileDownloadDTO {
    private Long id;
    private String fileName;
    private FileType fileType;
    private String fileUrl;
    private UUID uploadedBy;
    private LocalDateTime uploadedOn;
    private String content;
}
