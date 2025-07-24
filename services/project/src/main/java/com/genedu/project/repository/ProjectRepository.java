package com.genedu.project.repository;

import com.genedu.project.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Optional<Project> findByIdAndDeletedIsFalse(UUID id);
    Page<Project> findByUserIdAndDeletedIsFalse(UUID userId, Pageable pageable);
}
