package com.genedu.content.dto.subject;

import com.genedu.content.model.Subject;
import lombok.Builder;

@Builder
public record SubjectResponseDTO(Long id, String name, String description, Integer schoolClassId, String schoolClassName) {
}
