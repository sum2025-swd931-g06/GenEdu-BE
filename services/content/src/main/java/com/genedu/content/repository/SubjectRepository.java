package com.genedu.content.repository;

import com.genedu.content.model.Material;
import com.genedu.content.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Integer id);
    List<Subject> findBySchoolClass_Id(Integer schoolClassId);
}
