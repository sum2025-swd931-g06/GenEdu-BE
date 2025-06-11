package com.genedu.content.mapper;

import com.genedu.content.dto.schoolclass.SchoolClassRequestDTO;
import com.genedu.content.dto.schoolclass.SchoolClassResponseDTO;
import com.genedu.content.dto.subject.SubjectResponseDTO;
import com.genedu.content.model.SchoolClass;

import java.util.List;

public class SchoolClassMapper {
    public static SchoolClass toEntity(SchoolClassRequestDTO dto) {
        return SchoolClass.builder()
                .name(dto.name())
                .description(dto.description())
                .build();
    }

    public static SchoolClassResponseDTO toDTO(SchoolClass entity) {
        return SchoolClassResponseDTO.builder()
                .id(entity.getId().toString())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdOn(entity.getCreatedOn())
                .createdBy(entity.getCreatedBy())
                .lastModifiedOn(entity.getLastModifiedOn())
                .lastModifiedBy(entity.getLastModifiedBy())
                .build();
    }

    public static SchoolClassResponseDTO toDTOWithSubjects(SchoolClass entity, List<SubjectResponseDTO> subjects) {
        return SchoolClassResponseDTO.builder()
                .id(entity.getId().toString())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdOn(entity.getCreatedOn())
                .createdBy(entity.getCreatedBy())
                .lastModifiedOn(entity.getLastModifiedOn())
                .lastModifiedBy(entity.getLastModifiedBy())
                .subjects(subjects)
                .build();
    }
}
