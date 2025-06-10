package com.genedu.content.dto.subject;

import com.genedu.content.model.Subject;

public record SubjectRequestDTO(
        String name,
        String description
) {

    public SubjectRequestDTO {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
    }
}
