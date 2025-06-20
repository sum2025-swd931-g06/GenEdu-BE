package com.genedu.content.dto.chapter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.genedu.content.dto.lesson.LessonResponseDTO;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record ChapterResponseDTO(
        Long id,
        String title,
        Integer orderNumber,
        String description,
        ZonedDateTime createdOn,
        UUID createdBy,
        ZonedDateTime lastModifiedOn,
        UUID lastModifiedBy,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<LessonResponseDTO> lessons
) {}
