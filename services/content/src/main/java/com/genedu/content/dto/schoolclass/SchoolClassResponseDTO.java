package com.genedu.content.dto.schoolclass;

import com.genedu.content.dto.subject.SubjectResponseDTO;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record SchoolClassResponseDTO (
        String id,
        String name,
        String description,
        ZonedDateTime createdOn,
        UUID createdBy,
        ZonedDateTime lastModifiedOn,
        UUID lastModifiedBy,
        List<SubjectResponseDTO> subjects

) {
}

