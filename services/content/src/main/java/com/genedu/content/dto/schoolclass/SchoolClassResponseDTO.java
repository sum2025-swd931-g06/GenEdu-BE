package com.genedu.content.dto.schoolclass;

import com.genedu.content.dto.subject.SubjectResponseDTO;
import com.genedu.content.model.SchoolClass;
import lombok.Builder;

import java.util.List;

@Builder
public record SchoolClassResponseDTO(
        Integer id,
        String name,
        String description,
        List<SubjectResponseDTO> subjects
) {}
