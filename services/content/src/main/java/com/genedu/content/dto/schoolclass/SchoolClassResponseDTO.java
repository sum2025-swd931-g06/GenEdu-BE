package com.genedu.content.dto.schoolclass;

import com.genedu.content.dto.subject.SubjectResponseDTO;
import com.genedu.content.model.SchoolClass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.ZonedDateTime;
import java.util.List;

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

