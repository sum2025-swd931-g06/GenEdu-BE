package com.genedu.project.dto.client;

import com.genedu.commonlibrary.enumeration.FileType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class LectureFileDownloadDTO{
    private Long id;
    private String fileName;
    private FileType fileType;
    private String fileUrl;
    private UUID uploadedBy;
    private LocalDateTime uploadedOn;
}
