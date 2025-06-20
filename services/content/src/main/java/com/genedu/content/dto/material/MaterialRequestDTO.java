package com.genedu.content.dto.material;

public record MaterialRequestDTO(
        String title,
        String description,
        Integer orderNumber
){
    public MaterialRequestDTO {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
        if (orderNumber == null || orderNumber < 0) {
            throw new IllegalArgumentException("Order number must be a non-negative integer");
        }
    }
}
