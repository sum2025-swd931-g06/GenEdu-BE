package com.genedu.content.service;

import com.genedu.content.dto.subject.SubjectRequestDTO;
import com.genedu.content.dto.subject.SubjectResponseDTO;
import com.genedu.content.model.Subject;

import java.util.List;

public interface SubjectService {
    List<SubjectResponseDTO> getAllSubjects();
    Subject getSubjectEntityById(Long id);
    SubjectResponseDTO getSubjectById(Long id);
    SubjectResponseDTO createSubject(SubjectRequestDTO subjectRequestDTO);
    SubjectResponseDTO updateSubject(Long id, SubjectRequestDTO subjectRequestDTO);
    void deleteSubject(Long id);
}
