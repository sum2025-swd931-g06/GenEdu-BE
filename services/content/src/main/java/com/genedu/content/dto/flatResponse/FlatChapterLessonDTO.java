package com.genedu.content.dto.flatResponse;

import lombok.Builder;

@Builder
public record FlatChapterLessonDTO(
        Long chapterId,
        String chapterTitle,
        Integer chapterOrderNumber,
        String chapterDescription,

        Long lessonId,
        String lessonTitle,
        Integer lessonOrderNumber,
        String lessonDescription,
        String lessonStatus
) {
}
