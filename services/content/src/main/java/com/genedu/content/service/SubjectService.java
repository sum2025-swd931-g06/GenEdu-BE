package com.genedu.content.service;

import com.genedu.content.dto.flatResponse.FlatSchoolClassResponseDTO;
import com.genedu.content.dto.flatResponse.FlatSchoolClassSubjectDTO;
import com.genedu.content.dto.schoolclass.SchoolClassResponseDTO;
import com.genedu.content.dto.subject.SubjectRequestDTO;
import com.genedu.content.dto.subject.SubjectResponseDTO;
import com.genedu.content.model.Subject;

import java.util.List;

public interface SubjectService {
    List<FlatSchoolClassSubjectDTO> getAllSubjects();
    Subject getSubjectEntityById(Long id);
    FlatSchoolClassSubjectDTO getSubjectById(Long id);
    SchoolClassResponseDTO getSubjectsBySchoolClassId(Integer schoolClassId);
    FlatSchoolClassSubjectDTO createSubject(Integer schoolClassId, SubjectRequestDTO subjectRequestDTO);
    FlatSchoolClassSubjectDTO updateSubject(Long id, SubjectRequestDTO subjectRequestDTO);
    void deleteSubject(Long id);
}
