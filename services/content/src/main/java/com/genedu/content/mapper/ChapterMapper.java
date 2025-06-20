package com.genedu.content.mapper;

import com.genedu.content.dto.chapter.ChapterRequestDTO;
import com.genedu.content.dto.chapter.ChapterResponseDTO;
import com.genedu.content.dto.flatResponse.FlatMaterialChapterDTO;
import com.genedu.content.dto.lesson.LessonResponseDTO;
import com.genedu.content.model.Chapter;
import com.genedu.content.model.Material;

import java.util.List;

public class ChapterMapper {
    public static ChapterResponseDTO toDTO(Chapter chapter) {
        return ChapterResponseDTO.builder()
                .id(chapter.getId())
                .orderNumber(chapter.getOrderNumber())
                .title(chapter.getTitle())
                .description(chapter.getDescription())
                .createdOn(chapter.getCreatedOn())
                .createdBy(chapter.getCreatedBy())
                .lastModifiedOn(chapter.getLastModifiedOn())
                .lastModifiedBy(chapter.getLastModifiedBy())
                .build();
    }

    public static ChapterResponseDTO toDTOWithLessons(Chapter chapter, List<LessonResponseDTO> lessons) {
        return ChapterResponseDTO.builder()
                .id(chapter.getId())
                .orderNumber(chapter.getOrderNumber())
                .title(chapter.getTitle())
                .description(chapter.getDescription())
                .lessons(lessons)

                .createdOn(chapter.getCreatedOn())
                .createdBy(chapter.getCreatedBy())
                .lastModifiedOn(chapter.getLastModifiedOn())
                .lastModifiedBy(chapter.getLastModifiedBy())
                .build();
    }

    public static FlatMaterialChapterDTO toFlatDTO(Chapter chapter) {
        return FlatMaterialChapterDTO.builder()
                .materialId(chapter.getMaterial().getId())
                .materialName(chapter.getMaterial().getSubject().getName())
                .materialDescription(chapter.getMaterial().getDescription())

                .chapterId(chapter.getId())
                .chapterTitle(chapter.getTitle())
                .chapterOrderNumber(chapter.getOrderNumber())
                .chapterDescription(chapter.getDescription())
                .build();
    }


    public static Chapter toEntity(ChapterRequestDTO chapterRequestDTO, Material material) {
        if (chapterRequestDTO == null || material == null) {
            throw new IllegalArgumentException("ChapterRequestDTO and Material cannot be null");
        }
        return Chapter.builder()
                .orderNumber(chapterRequestDTO.orderNumber())
                .title(chapterRequestDTO.title())
                .description(chapterRequestDTO.description())
                .material(material)
                .build();
    }

}
