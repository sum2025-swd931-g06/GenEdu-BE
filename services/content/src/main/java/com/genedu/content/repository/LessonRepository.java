package com.genedu.content.repository;

import com.genedu.content.model.Lesson;
import com.genedu.content.model.LessonContent;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllByChapterId(Long chapterId);
    boolean existsByChapter_IdAndOrderNumber(Long chapterId, Integer orderNumber);
    boolean existsByChapter_IdAndOrderNumberAndIdNot(Long chapterId, Integer orderNumber, Long lessonId);
    Lesson findLessonByChapter_IdAndOrderNumberAndIdNot(Long chapterId, Integer orderNumber, Long lessonId);
    List<Lesson> findByChapterId(Long chapterId);

    @EntityGraph(
            attributePaths = {
                    "chapter",
                    "chapter.material",
                    "chapter.material.subject",
                    "chapter.material.subject.schoolClass",
            }
    )
    Lesson findLessonById(Long lessonId);
}
