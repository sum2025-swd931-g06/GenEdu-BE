package com.genedu.project.model.enumeration;

import lombok.Getter;

@Getter
public enum ProjectStatus {
    DRAFT("Draft"),
    GENERATING("Generating"),
    COMPLETED("Completed"),
    FAILED("Failed");

    private final String name;

    ProjectStatus(String name) {
        this.name = name;
    }
}
