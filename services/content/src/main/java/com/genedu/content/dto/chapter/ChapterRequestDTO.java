package com.genedu.content.dto.chapter;

public record ChapterRequestDTO (Integer orderNumber, String title, String description, Integer subjectId) {

    public ChapterRequestDTO {
        if (orderNumber == null || orderNumber < 0) {
            throw new IllegalArgumentException("Order number must be a non-negative integer.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank.");
        }
        if (subjectId == null || subjectId <= 0) {
            throw new IllegalArgumentException("Subject ID must be a positive integer.");
        }
    }
}
