package com.genedu.content.service;

import com.genedu.content.dto.chapter.ChapterResponseDTO;
import com.genedu.content.dto.flatResponse.FlatSubjectChapterLessonDTO;
import com.genedu.content.dto.lesson.LessonRequestDTO;
import com.genedu.content.dto.lesson.LessonResponseDTO;

import java.util.List;

public interface LessonService {
    List<FlatSubjectChapterLessonDTO> getAllLessons();
    List<LessonResponseDTO> getAllLessonsByChapterId(Long chapterId);
    ChapterResponseDTO getChapterLessonsById(Long id);
    FlatSubjectChapterLessonDTO getLessonById(Long lessonId);
    FlatSubjectChapterLessonDTO createLesson(Long chapterId, LessonRequestDTO lessonRequestDTO);
    FlatSubjectChapterLessonDTO updateLesson(Long lessonId, LessonRequestDTO lessonRequestDTO);
    void deleteLesson(Long lessonId);

}
