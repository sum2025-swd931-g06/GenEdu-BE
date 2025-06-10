package com.genedu.content.service;

import com.genedu.content.dto.schoolclass.SchoolClassRequestDTO;
import com.genedu.content.dto.schoolclass.SchoolClassResponseDTO;
import com.genedu.content.model.SchoolClass;

import java.util.List;

public interface SchoolClassService {
//    List<SchoolClassResponseDTO> getAllSchoolClasses(int pageNo);
    List<SchoolClassResponseDTO> getAllSchoolClasses();
    SchoolClassResponseDTO getSchoolClassById(Integer id);
    SchoolClass getSchoolClassEntityById(Integer id);
    SchoolClassResponseDTO createSchoolClass(SchoolClassRequestDTO schoolClassRequestDTO);
    SchoolClassResponseDTO updateSchoolClass(Integer id, SchoolClassRequestDTO schoolClassRequestDTO);
    void deleteSchoolClass(Integer id);
}
