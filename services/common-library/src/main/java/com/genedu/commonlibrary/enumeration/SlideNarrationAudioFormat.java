package com.genedu.commonlibrary.enumeration;

public enum SlideNarrationAudioFormat {
    MP3("mp3"),
    WAV("wav"),
    M4A("m4a");

    private final String extension;
    public String getExtension() {
        return extension;
    }
    SlideNarrationAudioFormat(String extension) {
        this.extension = extension;
    }
}
