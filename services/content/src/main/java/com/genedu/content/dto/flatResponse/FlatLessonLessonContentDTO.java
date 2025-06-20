package com.genedu.content.dto.flatResponse;

import lombok.Builder;

@Builder
public record FlatLessonLessonContentDTO(
        Long lessonId,
        String lessonTitle,
        String lessonDescription,
        Integer lessonOrderNumber,

        Long lessonContentId,
        String lessonContentTitle,
        Integer lessonContentOrderNumber,
        String lessonContentContent
) {}
