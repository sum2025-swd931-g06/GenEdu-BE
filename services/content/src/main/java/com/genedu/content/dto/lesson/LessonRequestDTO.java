package com.genedu.content.dto.lesson;

public record LessonRequestDTO (
        String title,
        String description,
        Integer orderNumber
){
    public LessonRequestDTO {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
        if (orderNumber == null || orderNumber < 0) {
            throw new IllegalArgumentException("Order number must be a non-negative integer");
        }
    }
}
