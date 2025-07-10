package com.genedu.commonlibrary.enumeration;

public enum FileType {
    AUDIO("Audio"),
    VIDEO("Video"),
    IMAGE("Image"),
    DOCUMENT("Document"),
    SLIDE("Slide"),
    LESSON_PLAN("LESSON_PLAN"),
    LESSON_PLAN_TEMPLATE("LESSON_PLAN_TEMPLATE")
    ;

    private final String name;

    FileType(String name) {
        this.name = name;
    }
}
