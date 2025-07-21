package com.genedu.commonlibrary.enumeration;

public enum PaymentStatus {
    PENDING("pending"),
    COMPLETED("completed"),
    FAILED("failed"),
    CANCELED("canceled"),
    REFUNDED("refunded");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
