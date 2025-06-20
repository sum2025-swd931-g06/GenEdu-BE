package com.genedu.content.mapper;

import com.genedu.content.dto.flatResponse.FlatLessonLessonContentDTO;
import com.genedu.content.dto.lessoncontent.LessonContentRequestDTO;
import com.genedu.content.dto.lessoncontent.LessonContentResponseDTO;
import com.genedu.content.dto.lessoncontentmediafile.LessonContentMediaFileResponseDTO;
import com.genedu.content.model.Lesson;
import com.genedu.content.model.LessonContent;

import java.util.List;

public class LessonContentMapper {
    public static LessonContentResponseDTO toDTO(LessonContent lessonContent) {

        return LessonContentResponseDTO.builder()
                .id(lessonContent.getId())
                .orderNumber(lessonContent.getOrderNumber())
                .title(lessonContent.getTitle())
                .content(lessonContent.getContent())
                .createdOn(lessonContent.getCreatedOn())
                .createdBy(lessonContent.getCreatedBy())
                .lastModifiedOn(lessonContent.getLastModifiedOn())
                .lastModifiedBy(lessonContent.getLastModifiedBy())
                .build();
    }

    public static LessonContentResponseDTO toDTOWithMediaFiles(LessonContent lessonContent,
                                                               List<LessonContentMediaFileResponseDTO> mediaFiles) {
        return LessonContentResponseDTO.builder()
                .id(lessonContent.getId())
                .orderNumber(lessonContent.getOrderNumber())
                .title(lessonContent.getTitle())
                .content(lessonContent.getContent())
                .mediaFiles(mediaFiles)

                .createdOn(lessonContent.getCreatedOn())
                .createdBy(lessonContent.getCreatedBy())
                .lastModifiedOn(lessonContent.getLastModifiedOn())
                .lastModifiedBy(lessonContent.getLastModifiedBy())
                .build();
    }

    public static FlatLessonLessonContentDTO toFlatDTO(LessonContent lessonContent) {
        return FlatLessonLessonContentDTO.builder()
                .lessonId(lessonContent.getLesson().getId())
                .lessonTitle(lessonContent.getLesson().getTitle())
                .lessonDescription(lessonContent.getLesson().getDescription())
                .lessonOrderNumber(lessonContent.getLesson().getOrderNumber())

                .lessonContentId(lessonContent.getId())
                .lessonContentTitle(lessonContent.getTitle())
                .lessonContentOrderNumber(lessonContent.getOrderNumber())
                .lessonContentContent(lessonContent.getContent())
                .build();
    }

    public static LessonContent toEntity(LessonContentRequestDTO lessonContentRequestDTO, Lesson lesson) {
        if (lessonContentRequestDTO == null || lesson == null) {
            throw new IllegalArgumentException("LessonContentRequestDTO and Lesson cannot be null");
        }
        return LessonContent.builder()
                .title(lessonContentRequestDTO.title())
                .orderNumber(lessonContentRequestDTO.orderNumber())
                .content(lessonContentRequestDTO.content())
                .lesson(lesson)
                .build();
    }
}
