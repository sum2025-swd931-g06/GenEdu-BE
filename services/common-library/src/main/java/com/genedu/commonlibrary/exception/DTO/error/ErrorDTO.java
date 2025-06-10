package com.genedu.commonlibrary.exception.DTO.error;

import java.util.ArrayList;
import java.util.List;

public record ErrorDTO(String statusCode, String title, String detail, List<String> fieldErrors) {
    public ErrorDTO(String statusCode, String title, String detail) {
        this(statusCode, title, detail, new ArrayList<>());
    }
}
