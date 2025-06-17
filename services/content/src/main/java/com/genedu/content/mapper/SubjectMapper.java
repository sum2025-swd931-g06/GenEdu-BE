package com.genedu.content.mapper;

import com.genedu.content.dto.chapter.ChapterResponseDTO;
import com.genedu.content.dto.flatResponse.FlatSchoolClassSubjectDTO;
import com.genedu.content.dto.material.MaterialResponseDTO;
import com.genedu.content.dto.subject.SubjectRequestDTO;
import com.genedu.content.dto.subject.SubjectResponseDTO;
import com.genedu.content.model.Material;
import com.genedu.content.model.SchoolClass;
import com.genedu.content.model.Subject;

import java.util.List;

public class SubjectMapper {
    public static SubjectResponseDTO toDTO(Subject subject) {
        return SubjectResponseDTO.builder()
                .id(subject.getId())
                .name(subject.getName())
                .description(subject.getDescription())
                .createdOn(subject.getCreatedOn())
                .createdBy(subject.getCreatedBy())
                .lastModifiedOn(subject.getLastModifiedOn())
                .lastModifiedBy(subject.getLastModifiedBy())
                .build();
    }

    public static FlatSchoolClassSubjectDTO toFlatDTO(Subject subject) {
        return FlatSchoolClassSubjectDTO.builder()
                .schoolClassId(subject.getSchoolClass().getId())
                .schoolClassName(subject.getSchoolClass().getName())
                .schoolClassDescription(subject.getSchoolClass().getDescription())
                .subjectId(subject.getId())
                .subjectName(subject.getName())
                .subjectDescription(subject.getDescription())
                .build();
    }

    public static SubjectResponseDTO toDTOWithChapters(Subject material, List<MaterialResponseDTO> materialResponseDTOS) {
        return SubjectResponseDTO.builder()
                .id(material.getId())
                .name(material.getName())
                .description(material.getDescription())
                .materials(materialResponseDTOS)
                .createdOn(material.getCreatedOn())
                .createdBy(material.getCreatedBy())
                .lastModifiedOn(material.getLastModifiedOn())
                .lastModifiedBy(material.getLastModifiedBy())
                .build();
    }

    public static Subject toEntity(SubjectRequestDTO subjectRequestDTO, SchoolClass schoolClass) {
        return Subject.builder()
                .name(subjectRequestDTO.name())
                .description(subjectRequestDTO.description())
                .schoolClass(schoolClass)
                .build();
    }
}
