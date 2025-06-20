package com.genedu.content.repository;

import com.genedu.content.model.Chapter;
import com.genedu.content.model.LessonContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonContentRepository extends JpaRepository<LessonContent, Long> {
    boolean existsByOrderNumberAndLessonId(int orderNumber, Long lessonId);
    boolean existsByOrderNumberAndLessonIdAndIdNot(int orderNumber, Long lessonId, Long id);
    List<LessonContent> findByLessonId(Long lessonId);
    List<LessonContent> findByLessonIdAndOrderNumber(Long materialId, int orderNumber);
}
