package com.genedu.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public record SlideContentRequestDTO(
        @JsonIgnore
        String lectureContentId,
        String title,
        String slideType,
        Integer orderNumber,
        Map<String, Object> subpoints,
        String narrationScript
) {
}
