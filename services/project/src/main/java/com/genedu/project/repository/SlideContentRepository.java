package com.genedu.project.repository;

import com.genedu.project.model.SlideContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SlideContentRepository extends JpaRepository<SlideContent, UUID> {
}
