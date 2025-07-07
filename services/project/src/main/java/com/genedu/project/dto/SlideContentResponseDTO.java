package com.genedu.project.dto;

import java.util.Map;

public record SlideContentResponseDTO(
        String lectureContentId,
        String title,
        String slideType,
        Integer orderNumber,
        Map<String, Object> subpoints,
        String narrationScript
) {
}
