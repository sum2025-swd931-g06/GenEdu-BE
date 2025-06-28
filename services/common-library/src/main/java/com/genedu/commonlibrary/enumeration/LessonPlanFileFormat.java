package com.genedu.commonlibrary.enumeration;

public enum LessonPlanFileFormat {
    DOCX("docx"),
    PDF("doc"),
    TXT("txt");

    private final String extension;

    public String getExtension() {
        return extension;
    }
    LessonPlanFileFormat(String extension) {
        this.extension = extension;
    }
}
