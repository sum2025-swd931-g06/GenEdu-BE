package com.genedu.content.dto.lesson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.genedu.content.dto.lessoncontent.LessonContentResponseDTO;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record LessonResponseDTO(
        Long id,
        String title,
        Integer orderNumber,
        String description,
        ZonedDateTime createdOn,
        UUID createdBy,
        ZonedDateTime lastModifiedOn,
        UUID lastModifiedBy,
        String status,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<LessonContentResponseDTO> lessonContents
) {}
