package com.genedu.content.dto.subject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.genedu.content.dto.chapter.ChapterResponseDTO;
import com.genedu.content.model.Subject;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
public record SubjectResponseDTO(
        Long id,
        String name,
        String description,
        ZonedDateTime createdOn,
        String createdBy,
        ZonedDateTime lastModifiedOn,
        String lastModifiedBy,
        
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<ChapterResponseDTO> chapters
) {}