package com.genedu.content.dto.schoolclass;

import com.genedu.content.dto.subject.SubjectResponseDTO;

import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
public record SchoolClassResponseDTO (
        String id,
        String name,
        String description,
        ZonedDateTime createdOn,
        String createdBy,
        ZonedDateTime lastModifiedOn,
        String lastModifiedBy,
        List<SubjectResponseDTO> subjects

) {
}

