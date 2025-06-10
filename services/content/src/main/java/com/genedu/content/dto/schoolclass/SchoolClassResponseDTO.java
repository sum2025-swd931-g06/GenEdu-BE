package com.genedu.content.dto.schoolclass;

import com.genedu.content.model.SchoolClass;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.ZonedDateTime;

public record SchoolClassResponseDTO (
        String id,
        String name,
        String description,
        ZonedDateTime createdOn,
        String createdBy,
        ZonedDateTime lastModifiedOn,
        String lastModifiedBy

) {
    public static SchoolClassResponseDTO fromSchoolClass(SchoolClass schoolClass) {
        return new SchoolClassResponseDTO(
                schoolClass.getId().toString(),
                schoolClass.getName(),
                schoolClass.getDescription(),
                schoolClass.getCreatedOn(),
                schoolClass.getCreatedBy(),
                schoolClass.getLastModifiedOn(),
                schoolClass.getLastModifiedBy()
        );
    }
}
