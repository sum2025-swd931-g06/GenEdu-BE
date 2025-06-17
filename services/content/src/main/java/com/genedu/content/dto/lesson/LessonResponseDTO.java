package com.genedu.content.dto.lesson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record LessonResponseDTO(
        Long id,
        String title,
        Integer orderNumber,
        String description,
        ZonedDateTime createdOn,
        String createdBy,
        ZonedDateTime lastModifiedOn,
        String lastModifiedBy
) {}
