package com.genedu.content.service;

import com.genedu.content.dto.schoolclass.SchoolClassRequestDTO;
import com.genedu.content.dto.schoolclass.SchoolClassResponseDTO;

import java.util.List;

public interface SchoolClassService {
//    List<SchoolClassResponseDTO> getAllSchoolClasses(int pageNo);
    List<SchoolClassResponseDTO> getAllSchoolClasses();
    SchoolClassResponseDTO createSchoolClass(SchoolClassRequestDTO schoolClassRequestDTO);
    SchoolClassResponseDTO updateSchoolClass(Integer id, SchoolClassRequestDTO schoolClassRequestDTO);
    void deleteSchoolClass(Integer id);
}
