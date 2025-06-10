package com.genedu.content.dto.subject;

import com.genedu.content.model.Subject;

public record SubjectRequestDTO(String name, String description, Integer schoolClassId) {

    public SubjectRequestDTO {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (schoolClassId == null || schoolClassId <= 0) {
            throw new IllegalArgumentException("School class ID must be a positive integer");
        }
    }
}
