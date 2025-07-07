package com.genedu.content.dto.flatResponse;

import lombok.Builder;

@Builder
public record FlatLessonContentLessonContentMediaFile(
        Long lessonContentId,
        String lessonContentTitle,
        String lessonContentContent,
        Integer lessonContentOrderNumber,

        Long id,
        Long lessonContentMediaFileId,
        Integer lessonContentMediaFileOrderNumber,
        String lessonContentMediaFileDescription
) {}
