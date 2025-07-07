package com.genedu.lecturecontent.dto;
import lombok.Data;

public record LectureContentRequestDTO(
    String schoolClassId,
    String subjectId,
    String materialId,
    String chapterId,
    String lessonId,
    String lessonContentId,
    String content
) {
    public LectureContentRequestDTO {
        if (schoolClassId == null || schoolClassId.isBlank()) {
            throw new IllegalArgumentException("School class ID cannot be blank");
        }
        if (subjectId == null || subjectId.isBlank()) {
            throw new IllegalArgumentException("Subject ID cannot be blank");
        }
        if (materialId == null || materialId.isBlank()) {
            throw new IllegalArgumentException("Material ID cannot be blank");
        }
        if (chapterId == null || chapterId.isBlank()) {
            throw new IllegalArgumentException("Chapter ID cannot be blank");
        }
        if (lessonId == null || lessonId.isBlank()) {
            throw new IllegalArgumentException("Lesson ID cannot be blank");
        }
        if (lessonContentId == null || lessonContentId.isBlank()) {
            throw new IllegalArgumentException("Lesson content ID cannot be blank");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be blank");
        }
    }
}