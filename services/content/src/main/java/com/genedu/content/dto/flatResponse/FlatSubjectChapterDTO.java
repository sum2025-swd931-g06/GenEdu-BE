package com.genedu.content.dto.flatResponse;

import lombok.Builder;

@Builder
public record FlatSubjectChapterDTO (
        Long subjectId,
        String subjectName,
        String subjectDescription,

        Long chapterId,
        String chapterTitle,
        Integer chapterOrderNumber,
        String chapterDescription
) {}