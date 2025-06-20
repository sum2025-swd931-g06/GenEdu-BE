package com.genedu.content.dto.subject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.genedu.content.dto.chapter.ChapterResponseDTO;
import com.genedu.content.dto.material.MaterialResponseDTO;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record SubjectResponseDTO(
        Integer id,
        String name,
        String description,
        ZonedDateTime createdOn,
        UUID createdBy,
        ZonedDateTime lastModifiedOn,
        UUID lastModifiedBy,
        
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<MaterialResponseDTO> materials
) {}