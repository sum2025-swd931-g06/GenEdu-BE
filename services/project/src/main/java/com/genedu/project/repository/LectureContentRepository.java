package com.genedu.project.repository;

import com.genedu.project.model.LectureContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LectureContentRepository extends JpaRepository<LectureContent, UUID> {
    LectureContent findLastByProjectIdAndDeletedIsFalseOrderByCreatedOnDesc(UUID projectId);
}
