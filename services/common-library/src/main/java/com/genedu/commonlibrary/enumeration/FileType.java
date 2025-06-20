package com.genedu.commonlibrary.enumeration;

public enum FileType {
    AUDIO("Audio"),
    VIDEO("Video"),
    IMAGE("Image"),
    DOCUMENT("Document"),
    PRESENTATION("Presentation"),
    LECTURE("Lecture"),;

    private final String name;

    FileType(String name) {
        this.name = name;
    }
}
