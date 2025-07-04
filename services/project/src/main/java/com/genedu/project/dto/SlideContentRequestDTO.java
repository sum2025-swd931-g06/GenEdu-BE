package com.genedu.project.dto;

import java.util.Map;

public record SlideContentRequestDTO(
        String lectureContentId,
        String title,
        String mainIdea,
        Integer orderNumber,
        Map<String, Object> subpoints,
        String narrationScript
) {
}
