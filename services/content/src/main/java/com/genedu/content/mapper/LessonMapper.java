package com.genedu.content.mapper;

import com.genedu.content.dto.flatResponse.FlatChapterLessonDTO;
import com.genedu.content.dto.flatResponse.FlatSubjectChapterLessonDTO;
import com.genedu.content.dto.lesson.LessonRequestDTO;
import com.genedu.content.dto.lesson.LessonResponseDTO;
import com.genedu.content.model.Chapter;
import com.genedu.content.model.Lesson;

public class LessonMapper {
    public static LessonResponseDTO toDTO(Lesson lesson) {
        return LessonResponseDTO.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .orderNumber(lesson.getOrderNumber())

                .createdOn(lesson.getCreatedOn())
                .createdBy(lesson.getCreatedBy())
                .lastModifiedOn(lesson.getLastModifiedOn())
                .lastModifiedBy(lesson.getLastModifiedBy())
                .build();
    }

    public static FlatChapterLessonDTO toDTOWithChapter(Lesson lesson) {
        return FlatChapterLessonDTO.builder()
                .chapterId(lesson.getChapter().getId())
                .chapterTitle(lesson.getChapter().getTitle())
                .chapterOrderNumber(lesson.getChapter().getOrderNumber())
                .chapterDescription(lesson.getChapter().getDescription())

                .lessonId(lesson.getId())
                .lessonTitle(lesson.getTitle())
                .lessonOrderNumber(lesson.getOrderNumber())
                .lessonDescription(lesson.getDescription())
                .build();
    }

    public static Lesson toEntity(LessonRequestDTO lessonRequestDTO, Chapter chapter) {
        return Lesson.builder()
                .title(lessonRequestDTO.title())
                .description(lessonRequestDTO.description())
                .orderNumber(lessonRequestDTO.orderNumber())
                .chapter(chapter)
                .build();
    }
}
