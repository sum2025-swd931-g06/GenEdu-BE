package com.genedu.lecturecontent.dto;

import java.util.Map;

public record Slide(
        String type, String title, Map<String, Object> data, String narrationScript
) {
}
