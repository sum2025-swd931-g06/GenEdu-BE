package com.genedu.content.service;

import com.genedu.content.dto.chapter.ChapterResponseDTO;
import com.genedu.content.dto.flatResponse.FlatChapterLessonDTO;
import com.genedu.content.dto.lesson.LessonRequestDTO;
import com.genedu.content.model.Lesson;

import java.util.List;

public interface LessonService {
    List<FlatChapterLessonDTO> getAllLessons();
    Lesson getLessonEntityById(Long lessonId);
    FlatChapterLessonDTO getLessonById(Long lessonId);
    ChapterResponseDTO getLessonsByChapterId(Long chapterId);

    FlatChapterLessonDTO createLesson(Long chapterId, LessonRequestDTO lessonRequestDTO);
    FlatChapterLessonDTO updateLesson(Long lessonId, LessonRequestDTO lessonRequestDTO);
    void deleteLesson(Long lessonId);
}
