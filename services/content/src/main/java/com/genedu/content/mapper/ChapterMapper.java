package com.genedu.content.mapper;

import com.genedu.content.dto.chapter.ChapterResponseDTO;
import com.genedu.content.model.Chapter;

public class ChapterMapper {
    public static ChapterResponseDTO toDTO(Chapter chapter) {
        if (chapter == null) {
            return null;
        }
        return ChapterResponseDTO.builder()
                .id(chapter.getId())
                .orderNumber(chapter.getOrderNumber())
                .title(chapter.getTitle())
                .description(chapter.getDescription())
                .subjectId(chapter.getSubject() != null ? chapter.getSubject().getId() : null)
                .build();
    }
}
