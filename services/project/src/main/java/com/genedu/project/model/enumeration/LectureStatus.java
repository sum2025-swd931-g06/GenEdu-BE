package com.genedu.project.model.enumeration;

import lombok.Getter;

@Getter
public enum LectureStatus {
    DRAFT("Draft"),
    FINALIZED("Finalized"),
    FAILED("Failed");

    private final String name;

    LectureStatus(String name) {
        this.name = name;
    }
}
