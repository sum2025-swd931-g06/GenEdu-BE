package com.genedu.content.service;

import com.genedu.content.dto.flatResponse.FlatSchoolClassSubjectDTO;
import com.genedu.content.dto.flatResponse.FlatSubjectMaterialDTO;
import com.genedu.content.dto.material.MaterialRequestDTO;
import com.genedu.content.dto.schoolclass.SchoolClassResponseDTO;
import com.genedu.content.dto.subject.SubjectRequestDTO;
import com.genedu.content.dto.subject.SubjectResponseDTO;
import com.genedu.content.model.Material;
import com.genedu.content.model.Subject;
import org.springframework.stereotype.Service;

import java.util.List;


public interface MaterialService {
    List<FlatSubjectMaterialDTO> getAllMaterials();
    Material getMaterialEntityById(Long id);
    FlatSubjectMaterialDTO getMaterialById(Long id);
    SubjectResponseDTO getMaterialsBySubjectId(Integer subjectId);
    FlatSubjectMaterialDTO createMaterial(Integer subjectId, MaterialRequestDTO materialRequestDTO);
    FlatSubjectMaterialDTO updateMaterial(Integer id, MaterialRequestDTO materialRequestDTO);
    void deleteMaterial(Integer id);
}
