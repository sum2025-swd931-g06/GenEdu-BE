package com.genedu.commonlibrary.enumeration;

public enum LectureContentStatus {
    DRAFT("DRAFT"),
    GENERATED("GENERATED"),;

    private final String name;

    LectureContentStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
