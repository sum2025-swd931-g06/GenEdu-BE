package com.genedu.content.dto.lessoncontentmediafile;

import lombok.Builder;

@Builder
public record LessonContentMediaFileRequestDTO(
        Long mediaFileId,
        Integer orderNumber,
        String description
) {
    public LessonContentMediaFileRequestDTO {
        if (mediaFileId == null || mediaFileId <= 0) {
            throw new IllegalArgumentException("Media file ID must be a positive integer.");
        }
        if (orderNumber == null || orderNumber < 0) {
            throw new IllegalArgumentException("Order number must be a non-negative integer.");
        }
    }
}
