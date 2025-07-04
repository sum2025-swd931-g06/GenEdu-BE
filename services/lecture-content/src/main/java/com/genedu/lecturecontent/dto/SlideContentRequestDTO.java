package com.genedu.lecturecontent.dto;

public record SlideContentRequestDTO (
        String lessonId,
        String chapterId,
        String subjectId,
        String materialId,
        String schoolClassId,
        String lessonContentId,
        String CustomInstructions
){
}
