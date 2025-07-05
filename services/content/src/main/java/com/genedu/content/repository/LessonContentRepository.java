package com.genedu.content.repository;

import com.genedu.content.model.Chapter;
import com.genedu.content.model.LessonContent;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonContentRepository extends JpaRepository<LessonContent, Long> {
    boolean existsByOrderNumberAndLessonId(int orderNumber, Long lessonId);
    boolean existsByOrderNumberAndLessonIdAndIdNot(int orderNumber, Long lessonId, Long id);
    List<LessonContent> findByLessonId(Long lessonId);
    List<LessonContent> findByLessonIdAndOrderNumber(Long materialId, int orderNumber);

    @EntityGraph(
            attributePaths = {
                    "lesson",
                    "lesson.chapter",
                    "lesson.chapter.material",
                    "lesson.chapter.material.subject",
                    "lesson.chapter.material.subject.schoolClass",
            }
    )
    List<LessonContent> findAllByLessonId(Long lessonId);

}
