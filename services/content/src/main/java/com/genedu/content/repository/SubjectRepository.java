package com.genedu.content.repository;

import com.genedu.content.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}
