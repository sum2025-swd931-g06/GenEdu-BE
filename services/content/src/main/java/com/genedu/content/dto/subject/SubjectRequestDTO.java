package com.genedu.content.dto.subject;

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
