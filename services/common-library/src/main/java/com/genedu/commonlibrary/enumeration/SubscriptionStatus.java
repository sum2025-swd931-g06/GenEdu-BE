package com.genedu.commonlibrary.enumeration;

public enum SubscriptionStatus {
//    DOCX("docx"),
//    PDF("doc"),
//    TXT("txt");
//
//    private final String extension;
//
//    public String getExtension() {
//        return extension;
//    }
//    LessonPlanFileFormat(String extension) {
//        this.extension = extension;
//    }

    ACTIVE("ACTIVE"),
    CANCELED("CANCELED"),
    EXPIRED("EXPIRED"),
    CANCEL_AT_PERIOD_END("CANCEL_AT_PERIOD_END");

    private final String status;

    public String getStatus() {
        return status;
    }

    SubscriptionStatus(String status) {
        this.status = status;
    }
}
