package com.genedu.content.repository;

import com.genedu.content.model.LessonContentMediaFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonContentMediaFileRepository extends JpaRepository<LessonContentMediaFile, Long> {

    // Additional query methods can be defined here if needed
}
