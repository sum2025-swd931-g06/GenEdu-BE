package com.genedu.content.dto.lesson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.ZonedDateTime;
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
        UUID lastModifiedBy
) {}
