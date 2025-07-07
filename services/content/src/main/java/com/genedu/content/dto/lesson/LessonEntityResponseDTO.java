package com.genedu.content.dto.lesson;

public record LessonEntityResponseDTO (
        String schoolClassId,
        String subjectId,
        String materialId,
        String chapterId,
        String lessonId
) {
    public LessonEntityResponseDTO {
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
    }
}
