package com.genedu.content.dto.schoolclass;

public record SchoolClassRequestDTO(
        String name,
        String description
) {
    public SchoolClassRequestDTO {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
    }
}
