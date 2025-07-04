package com.genedu.content.dto.lessoncontent;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
public record LessonContentRequestDTO(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Long lessonContentId,
        String title,
        Integer orderNumber,
        String content
) {

    public LessonContentRequestDTO {
        if (orderNumber == null || orderNumber < 0) {
            throw new IllegalArgumentException("Order number must be a non-negative integer.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank.");
        }
    }
}
