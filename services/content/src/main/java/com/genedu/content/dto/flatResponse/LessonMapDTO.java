package com.genedu.content.dto.flatResponse;

import lombok.Builder;

@Builder
public record LessonMapDTO(
        Long lessonId,
        String lessonTitle,
        Long materialId
) {}
