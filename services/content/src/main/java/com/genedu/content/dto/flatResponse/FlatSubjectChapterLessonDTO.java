package com.genedu.content.dto.flatResponse;

import lombok.Builder;

@Builder
public record FlatSubjectChapterLessonDTO(
        Long subjectId,
        String subjectName,
        String subjectDescription,

        Long chapterId,
        String chapterTitle,
        Integer chapterOrderNumber,
        String chapterDescription,

        Long lessonId,
        String lessonTitle,
        Integer lessonOrderNumber,
        String lessonDescription
) {}
