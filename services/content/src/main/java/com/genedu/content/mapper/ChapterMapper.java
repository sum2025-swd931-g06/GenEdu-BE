package com.genedu.content.mapper;

import com.genedu.content.dto.chapter.ChapterRequestDTO;
import com.genedu.content.dto.chapter.ChapterResponseDTO;
import com.genedu.content.dto.flatResponse.FlatSubjectChapterDTO;
import com.genedu.content.dto.lesson.LessonResponseDTO;
import com.genedu.content.model.Chapter;
import com.genedu.content.model.Subject;

import java.util.List;

public class ChapterMapper {
    public static ChapterResponseDTO toDTO(Chapter chapter) {
        return ChapterResponseDTO.builder()
                .id(chapter.getId())
                .orderNumber(chapter.getOrderNumber())
                .title(chapter.getTitle())
                .description(chapter.getDescription())
                .build();
    }

    public static ChapterResponseDTO toDTOWithLessons(Chapter chapter, List<LessonResponseDTO> lessons) {
        return ChapterResponseDTO.builder()
                .id(chapter.getId())
                .orderNumber(chapter.getOrderNumber())
                .title(chapter.getTitle())
                .description(chapter.getDescription())
                .lessons(lessons)
                .build();
    }

    public static FlatSubjectChapterDTO toFlatDTO(Chapter chapter) {
        return FlatSubjectChapterDTO.builder()
                .subjectId(chapter.getSubject().getId())
                .subjectName(chapter.getSubject().getName())
                .subjectDescription(chapter.getSubject().getDescription())
                .chapterId(chapter.getId())
                .chapterTitle(chapter.getTitle())
                .chapterOrderNumber(chapter.getOrderNumber())
                .chapterDescription(chapter.getDescription())
                .build();
    }


    public static Chapter toEntity(ChapterRequestDTO chapterRequestDTO, Subject subject) {
        if (chapterRequestDTO == null || subject == null) {
            throw new IllegalArgumentException("ChapterRequestDTO and Subject cannot be null");
        }
        return Chapter.builder()
                .orderNumber(chapterRequestDTO.orderNumber())
                .title(chapterRequestDTO.title())
                .description(chapterRequestDTO.description())
                .subject(subject)
                .build();
    }
}
