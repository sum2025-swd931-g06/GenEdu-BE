package com.genedu.commonlibrary.enumeration;

public enum SlideFileFormat {
    PPTX("pptx");
    private final String extension;

    public String getExtension() {
        return extension;
    }
    SlideFileFormat(String extension) {
        this.extension = extension;
    }
}
