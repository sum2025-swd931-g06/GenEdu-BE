package com.genedu.content.dto.lessoncontentmediafile;

import lombok.Builder;

@Builder
public record LessonContentMediaFileResponseDTO(
        Long id,
        Long mediaFileId,
        Integer orderNumber,
        String description
) {
}
