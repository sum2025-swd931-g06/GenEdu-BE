package com.genedu.content.mapper;

import com.genedu.content.dto.flatResponse.FlatLessonContentLessonContentMediaFile;
import com.genedu.content.dto.lessoncontentmediafile.LessonContentMediaFileRequestDTO;
import com.genedu.content.dto.lessoncontentmediafile.LessonContentMediaFileResponseDTO;
import com.genedu.content.model.LessonContent;
import com.genedu.content.model.LessonContentMediaFile;

public class LessonContentMediaFileMapper {
    public static LessonContentMediaFileResponseDTO toDTO(LessonContentMediaFile lessonContentMediaFile) {
        return LessonContentMediaFileResponseDTO.builder()
                .id(lessonContentMediaFile.getId())
                .mediaFileId(lessonContentMediaFile.getMediaFileId())
                .orderNumber(lessonContentMediaFile.getOrderNumber())
                .description(lessonContentMediaFile.getDescription())
                .createdOn(lessonContentMediaFile.getCreatedOn())
                .createdBy(lessonContentMediaFile.getCreatedBy())
                .lastModifiedOn(lessonContentMediaFile.getLastModifiedOn())
                .lastModifiedBy(lessonContentMediaFile.getLastModifiedBy())
                .build();
    }
    public static FlatLessonContentLessonContentMediaFile toFlatDTO(LessonContentMediaFile lessonContentMediaFile) {
        return FlatLessonContentLessonContentMediaFile.builder()
                .lessonContentId(lessonContentMediaFile.getLessonContent().getId())
                .lessonContentTitle(lessonContentMediaFile.getLessonContent().getTitle())
                .lessonContentContent(lessonContentMediaFile.getLessonContent().getContent())
                .lessonContentOrderNumber(lessonContentMediaFile.getLessonContent().getOrderNumber())

                .id(lessonContentMediaFile.getId())
                .mediaFileId(lessonContentMediaFile.getMediaFileId())
                .orderNumber(lessonContentMediaFile.getOrderNumber())
                .description(lessonContentMediaFile.getDescription())

                .build();
    }

    public static LessonContentMediaFile toEntity(LessonContentMediaFileRequestDTO lessonContentMediaFileRequestDTO, LessonContent lessonContent) {
        if (lessonContentMediaFileRequestDTO == null || lessonContent == null) {
            throw new IllegalArgumentException("LessonContentMediaFileResponseDTO and LessonContent cannot be null");
        }
        return LessonContentMediaFile.builder()
                .mediaFileId(lessonContentMediaFileRequestDTO.mediaFileId())
                .orderNumber(lessonContentMediaFileRequestDTO.orderNumber())
                .description(lessonContentMediaFileRequestDTO.description())
                .lessonContent(lessonContent)
                .build();
    }

}
