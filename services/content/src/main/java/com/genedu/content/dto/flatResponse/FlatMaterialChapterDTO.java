package com.genedu.content.dto.flatResponse;

import lombok.Builder;

@Builder
public record FlatMaterialChapterDTO(
        Long materialId,
        String materialName,
        String materialDescription,

        Long chapterId,
        String chapterTitle,
        Integer chapterOrderNumber,
        String chapterDescription
) {}