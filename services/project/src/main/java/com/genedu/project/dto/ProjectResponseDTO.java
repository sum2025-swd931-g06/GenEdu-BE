package com.genedu.project.dto;

import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.project.model.enumeration.ProjectStatus;

import java.util.UUID;

public record ProjectResponseDTO(
     UUID id,
     UUID userId,
     Long lessonId,
     String title,
     String lessonPlanFileUrl,
     String customInstructions,
     ProjectStatus status,
     Integer slideNum,
     Long templateId
) {
    public ProjectResponseDTO {
        if (id == null || userId == null || lessonId == null || title == null) {
            throw new BadRequestException("ID, user ID, lesson ID, and title must not be null.");
        }
        if (title.isBlank() || title.length() > 255) {
            throw new BadRequestException("Title must not be blank and must be less than 255 characters.");
        }
        if (customInstructions != null && customInstructions.length() > 1000) {
            throw new BadRequestException("Custom instructions must be less than 1000 characters.");
        }
        if (slideNum != null && slideNum < 1) {
            throw new BadRequestException("Slide number must be at least 1.");
        }
    }
}
