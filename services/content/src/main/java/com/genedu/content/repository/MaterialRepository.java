package com.genedu.content.repository;

import com.genedu.content.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    boolean existsByOrOrderNumberAndSubject_Id(int orderNumber, Integer subjectId);
    boolean existsByOrOrderNumberAndSubject_IdAndIdNot(int orderNumber, Integer subjectId, Long id);
    List<Material> findBySubject_Id(Integer subjectId);
    List<Material> findBySubject_IdAndOrderNumber(Integer subjectId, int orderNumber);
}
