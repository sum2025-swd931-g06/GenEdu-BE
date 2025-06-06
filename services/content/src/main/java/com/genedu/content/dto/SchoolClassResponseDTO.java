package com.genedu.content.dto;

import com.genedu.content.model.SchoolClass;

public record SchoolClassResponseDTO(String id, String name, String description) {
    public static SchoolClassResponseDTO fromSchoolClass(SchoolClass schoolClass) {
        return new SchoolClassResponseDTO(
                schoolClass.getId().toString(),
                schoolClass.getName(),
                schoolClass.getDescription()
        );
    }
}
