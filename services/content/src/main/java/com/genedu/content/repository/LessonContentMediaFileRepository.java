package com.genedu.content.repository;

import com.genedu.content.model.LessonContentMediaFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonContentMediaFileRepository extends JpaRepository<LessonContentMediaFile, Long> {
    boolean existsByOrderNumberAndLessonContentId(int orderNumber, Long lessonContentId);
    boolean existsByOrderNumberAndLessonContentIdAndIdNot(int orderNumber, Long lessonContentId, Long id);
    List<LessonContentMediaFile> findByLessonContentId(Long lessonContentId);
}
