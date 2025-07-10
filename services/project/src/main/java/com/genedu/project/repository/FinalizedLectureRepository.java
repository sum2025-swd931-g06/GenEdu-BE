package com.genedu.project.repository;

import com.genedu.project.model.FinalizedLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface FinalizedLectureRepository extends JpaRepository<FinalizedLecture, UUID> {
    List<FinalizedLecture> findByLectureContentIdAndDeletedIsFalse(UUID lectureContentId);
}
