package com.genedu.lecturecontent.dto.webclient;

import java.util.Map;

public record SlideContentResponseDTO(
        String title,
        String mainIdea,
        Integer orderNumber,
        Map<String, Object> subpoints,
        String narrationScript
) {
}
