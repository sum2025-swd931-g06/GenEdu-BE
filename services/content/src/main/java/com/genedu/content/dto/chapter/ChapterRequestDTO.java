package com.genedu.content.dto.chapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.genedu.content.model.Chapter;

public record ChapterRequestDTO(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Long chapterId,
        Integer orderNumber,
        String title,
        String description
) {
    public ChapterRequestDTO {
        if (orderNumber == null || orderNumber < 0) {
            throw new IllegalArgumentException("Order number must be a non-negative integer.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank.");
        }
    }

}
