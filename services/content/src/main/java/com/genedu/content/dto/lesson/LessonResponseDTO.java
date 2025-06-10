package com.genedu.content.dto.lesson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
public record LessonResponseDTO(
        Long id,
        String title,
        Integer orderNumber,
        String description
) {}
