package com.genedu.content.mapper;

import com.genedu.content.dto.schoolclass.SchoolClassRequestDTO;
import com.genedu.content.dto.schoolclass.SchoolClassResponseDTO;
import com.genedu.content.model.SchoolClass;

public class SchoolClassMapper {
    public static SchoolClass toEntity(SchoolClassRequestDTO dto) {
        return SchoolClass.builder()
                .name(dto.name())
                .description(dto.description())
                .build();
    }

    public static SchoolClassResponseDTO toDTO(SchoolClass entity) {
        return new SchoolClassResponseDTO(
                entity.getId().toString(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedOn(),
                entity.getCreatedBy(),
                entity.getLastModifiedOn(),
                entity.getLastModifiedBy()
        );
    }
}
