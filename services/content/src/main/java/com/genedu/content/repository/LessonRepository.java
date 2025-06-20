package com.genedu.content.repository;

import com.genedu.content.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllByChapterId(Long chapterId);
    boolean existsByChapter_IdAndOrderNumber(Long chapterId, Integer orderNumber);
    boolean existsByChapter_IdAndOrderNumberAndIdNot(Long chapterId, Integer orderNumber, Long lessonId);
    Lesson findLessonByChapter_IdAndOrderNumberAndIdNot(Long chapterId, Integer orderNumber, Long lessonId);
    List<Lesson> findByChapterId(Long chapterId);
}
