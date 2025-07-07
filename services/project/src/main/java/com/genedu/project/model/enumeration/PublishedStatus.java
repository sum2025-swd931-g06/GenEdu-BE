package com.genedu.project.model.enumeration;

import lombok.Getter;

@Getter
public enum PublishedStatus {
    PRIVATE("Private"),
    PUBLIC("Public");

    private final String name;

    PublishedStatus(String name) {
        this.name = name;
    }
}
