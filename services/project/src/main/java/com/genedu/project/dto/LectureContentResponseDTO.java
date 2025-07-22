package com.genedu.project.dto;

import java.util.List;
import java.util.UUID;

public record LectureContentResponseDTO(
        UUID id,
        UUID projectId,
        String title,
        String status,
        List<SlideContentResponseDTO> slideContents
) {
}
