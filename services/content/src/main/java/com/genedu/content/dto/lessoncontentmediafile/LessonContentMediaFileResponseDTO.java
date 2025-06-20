package com.genedu.content.dto.lessoncontentmediafile;

import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
public record LessonContentMediaFileResponseDTO(
        Long id,
        Long mediaFileId,
        Integer orderNumber,
        String description
) {
}
