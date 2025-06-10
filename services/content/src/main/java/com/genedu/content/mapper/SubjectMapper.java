package com.genedu.content.mapper;

import com.genedu.content.dto.subject.SubjectRequestDTO;
import com.genedu.content.dto.subject.SubjectResponseDTO;
import com.genedu.content.model.SchoolClass;
import com.genedu.content.model.Subject;

public class SubjectMapper {
    public static SubjectResponseDTO toDTO(Subject subject) {
        if(subject == null) {
            return null;
        }
        return SubjectResponseDTO.builder()
                .id(subject.getId())
                .name(subject.getName())
                .description(subject.getDescription())
//                .schoolClassId(subject.getSchoolClass() != null ? subject.getSchoolClass().getId() : null)
//                .schoolClassName(subject.getSchoolClass() != null ? subject.getSchoolClass().getName() : null)
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
