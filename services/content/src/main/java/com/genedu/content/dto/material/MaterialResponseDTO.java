package com.genedu.content.dto.material;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.genedu.content.dto.chapter.ChapterResponseDTO;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
public record MaterialResponseDTO(
        Long id,
        String title,
        Integer orderNumber,
        String description,
        ZonedDateTime createdOn,
        String createdBy,
        ZonedDateTime lastModifiedOn,
        String lastModifiedBy,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<ChapterResponseDTO> chapters
) {}
