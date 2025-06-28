package com.genedu.project.dto;

import com.genedu.commonlibrary.exception.BadRequestException;

public record ProjectRequestDTO(
     Long lessonId,
     String title,
     String customInstructions,
     Integer slideNumber
) {
    public ProjectRequestDTO {
        if (lessonId == null || lessonId <= 0) {
            throw new BadRequestException("Lesson ID must be a positive number.");
        }
        if (title == null || title.isBlank() || title.length() > 255) {
            throw new BadRequestException("Title must not be blank and must be less than 255 characters.");
        }
        if (customInstructions != null && customInstructions.length() > 1000) {
            throw new BadRequestException("Custom instructions must be less than 1000 characters.");
        }
        if (slideNumber != null && slideNumber < 1) {
            throw new BadRequestException("Slide number must be at least 1.");
        }
    }
}
