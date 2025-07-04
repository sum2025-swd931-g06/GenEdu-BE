package com.genedu.content.repository;

import com.genedu.content.model.Material;
import com.genedu.content.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    boolean existsByNameAndSchoolClassId(String name, Integer schoolClassId);
    boolean existsByNameAndSchoolClassIdAndIdNot(String name, Integer schoolClassId, Integer id);
    List<Subject> findBySchoolClass_Id(Integer schoolClassId);
}
