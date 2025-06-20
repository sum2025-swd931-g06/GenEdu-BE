package com.genedu.content.repository;

import com.genedu.content.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    boolean existsByOrderNumberAndSubject_Id(int orderNumber, Integer subjectId);
    boolean existsByOrderNumberAndSubject_IdAndIdNot(int orderNumber, int subjectId, long id);
    List<Material> findBySubject_Id(Integer subjectId);
    List<Material> findBySubject_IdAndOrderNumber(Integer subjectId, int orderNumber);
}
