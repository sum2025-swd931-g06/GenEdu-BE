package com.genedu.content.repository;

import com.genedu.content.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    boolean existsByOrOrderNumberAndSubject_Id(int orderNumber, Long subjectId);
    boolean existsByOrderNumberAndSubject_IdAndIdNot(int orderNumber, Long subjectId, Long id);
    List<Chapter> findBySubject_Id(Long subjectId);
    List<Chapter> findBySubject_IdAndOrderNumber(Long subjectId, int orderNumber);
}
