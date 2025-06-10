package com.genedu.content.dto.chapter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.genedu.content.dto.lesson.LessonResponseDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record ChapterResponseDTO(
        Long id,
        String title,
        Integer orderNumber,
        String description,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<LessonResponseDTO> lessons
) {}
