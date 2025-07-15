package com.genedu.project.model.enumeration;

import lombok.Getter;

@Getter
public enum LectureStatus {
    DRAFT("DRAFT"),
    FINALIZED("FINALIZED"),
    FAILED("FAILED");

    private final String name;

    LectureStatus(String name) {
        this.name = name;
    }
}
