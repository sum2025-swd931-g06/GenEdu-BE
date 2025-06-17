package com.genedu.content.repository;

import com.genedu.content.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    boolean existsByOrOrderNumberAndMaterialId(int orderNumber, Long materialId);
    boolean existsByOrderNumberAndMaterial_IdAndIdNot(int orderNumber, Long materialId, Long id);
    List<Chapter> findByMaterial_Id(Long materialId);
    List<Chapter> findByMaterialIdAndOrderNumber(Long materialId, int orderNumber);
}
