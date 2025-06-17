package com.genedu.content.service.impl;

import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.content.dto.flatResponse.FlatSubjectMaterialDTO;
import com.genedu.content.dto.material.MaterialRequestDTO;
import com.genedu.content.dto.subject.SubjectResponseDTO;
import com.genedu.content.mapper.MaterialMapper;
import com.genedu.content.model.Material;
import com.genedu.content.repository.MaterialRepository;
import com.genedu.content.service.MaterialService;
import com.genedu.content.service.SubjectService;
import com.genedu.content.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@RequiredArgsConstructor
@Service
@Transactional
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final SubjectService subjectService;

    @Transactional(readOnly = true)
    @Override
    public List<FlatSubjectMaterialDTO> getAllMaterials() {
        return materialRepository.findAll().stream()
                .map(MaterialMapper::toFlatDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Material getMaterialEntityById(Long id) {
        validateMaterialId(id);
        return  materialRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.MATERIAL_NOT_FOUND, id));
    }

    @Transactional(readOnly = true)
    @Override
    public FlatSubjectMaterialDTO getMaterialById(Long id) {
        Material material = getMaterialEntityById(id);
        return MaterialMapper.toFlatDTO(material);
    }

    @Transactional(readOnly = true)
    @Override
    public SubjectResponseDTO getMaterialsBySubjectId(Integer subjectId) {
        return null;
    }

    @Override
    public FlatSubjectMaterialDTO createMaterial(Integer subjectId, MaterialRequestDTO materialRequestDTO) {
        return null;
    }

    @Override
    public FlatSubjectMaterialDTO updateMaterial(Integer id, MaterialRequestDTO materialRequestDTO) {
        return null;
    }

    @Override
    public void deleteMaterial(Integer id) {

    }

    // --- Private helpers ---
    private void validateMaterialId(Long id) {
        if (id == null) {
            throw new BadRequestException(Constants.ErrorCode.MATERIAL_ID_REQUIRED);
        }
    }
}
