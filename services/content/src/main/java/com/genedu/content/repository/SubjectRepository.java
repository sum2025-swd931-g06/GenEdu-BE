package com.genedu.content.repository;

import com.genedu.content.dto.subject.SubjectResponseDTO;
import com.genedu.content.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
    List<Subject> findBySchoolClass_Id(Integer schoolClassId);
}
