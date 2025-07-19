package com.genedu.commonlibrary.enumeration;

public enum TransactionStatus {
    SUCCESS ("Success"),
    FAILED ("Failed"),
    ;

    private final String status;
    TransactionStatus(String status) {
        this.status = status;
    }
}
