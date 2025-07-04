package com.genedu.project.dto;

import java.util.List;

public record LectureContentResponseDTO(
        String id,
        String projectId,
        String title,
        List<SlideContentRequestDTO> slideContents
) {
}
