package com.genedu.content.dto.material;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.genedu.content.dto.chapter.ChapterResponseDTO;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record MaterialResponseDTO(
        Long id,
        String title,
        Integer orderNumber,
        String description,
        ZonedDateTime createdOn,
        UUID createdBy,
        ZonedDateTime lastModifiedOn,
        UUID lastModifiedBy,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<ChapterResponseDTO> chapters
) {}
