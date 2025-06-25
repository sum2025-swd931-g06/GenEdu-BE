package com.genedu.commonlibrary.enumeration;

public enum LessonStatus {
    UN_SYNCED("UN_SYNC"),
    SYNCED("SYNCED");

    private final String name;
    LessonStatus(String name) {
        this.name = name;
    }
}
