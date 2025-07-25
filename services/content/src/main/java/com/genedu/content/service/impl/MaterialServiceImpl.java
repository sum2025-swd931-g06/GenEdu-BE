package com.genedu.content.service.impl;

import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.DuplicatedException;
import com.genedu.commonlibrary.exception.InternalServerErrorException;
import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.content.dto.flatResponse.FlatSubjectMaterialDTO;
import com.genedu.content.dto.material.MaterialRequestDTO;
import com.genedu.content.dto.material.MaterialResponseDTO;
import com.genedu.content.dto.subject.SubjectResponseDTO;
import com.genedu.content.mapper.MaterialMapper;
import com.genedu.content.mapper.SubjectMapper;
import com.genedu.content.model.Material;
import com.genedu.content.model.Subject;
import com.genedu.content.repository.MaterialRepository;
import com.genedu.content.service.MaterialService;
import com.genedu.content.service.SubjectService;
import com.genedu.content.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
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
        validateIdNotNull(id, Constants.ErrorCode.MATERIAL_ID_REQUIRED);
        return materialRepository.findById(id)
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
        Subject subject = subjectService.getSubjectEntityById(subjectId);
        List<MaterialResponseDTO> materials = materialRepository.findBySubject_Id(subjectId)
                .stream()
                .map(MaterialMapper::toDTO)
                .toList();
        return SubjectMapper.toDTOWithMaterials(subject, materials);
    }

    @Override
    public FlatSubjectMaterialDTO createMaterial(Integer subjectId, MaterialRequestDTO materialRequestDTO) {
        if(materialRepository.existsByOrderNumberAndSubject_Id(materialRequestDTO.orderNumber(), subjectId)){
            throw new DuplicatedException(Constants.ErrorCode.DUPLICATED_MATERIAL_ORDER, materialRequestDTO.orderNumber());
        }

        if(materialRequestDTO.orderNumber() < 1) {
            throw new BadRequestException(Constants.ErrorCode.MATERIAL_ORDER_NUMBER_INVALID);
        }
        Subject subject = subjectService.getSubjectEntityById(subjectId);

        Material createdMaterial = MaterialMapper.toEntity(materialRequestDTO, subject);

        try {
            Material saveMaterial = materialRepository.save(createdMaterial);
            return MaterialMapper.toFlatDTO(saveMaterial);
        } catch (Exception e){
            log.error("Error creating material");
            throw new InternalServerErrorException(Constants.ErrorCode.CREATE_MATERIAL_FAILED, e.getMessage());
        }
    }

    @Override
    public FlatSubjectMaterialDTO updateMaterial(Long materialId , MaterialRequestDTO materialRequestDTO) {
        Material existingMaterial = getMaterialEntityById(materialId);

        if(materialRepository.existsByOrderNumberAndSubject_IdAndIdNot(materialRequestDTO.orderNumber(), existingMaterial.getSubject().getId(), materialId)){
            throw new DuplicatedException(Constants.ErrorCode.DUPLICATED_MATERIAL_ORDER, materialRequestDTO.orderNumber());
        }

        existingMaterial.setTitle(materialRequestDTO.title());
        existingMaterial.setOrderNumber(materialRequestDTO.orderNumber());
        existingMaterial.setDescription(materialRequestDTO.description());
        try {
            Material updatedMaterial = materialRepository.save(existingMaterial);
            return MaterialMapper.toFlatDTO(updatedMaterial);
        } catch (Exception e) {
            log.error("Error updating material", e);
            throw new InternalServerErrorException(Constants.ErrorCode.UPDATE_MATERIAL_FAILED, e.getMessage());
        }
    }

    @Override
    public void deleteMaterial(Long id) {
        validateIdNotNull(id, Constants.ErrorCode.MATERIAL_ID_REQUIRED);

        if (!materialRepository.existsById(id)) {
            throw new NotFoundException(Constants.ErrorCode.MATERIAL_NOT_FOUND, id);
        }

        try {
            Material existingMaterial = getMaterialEntityById(id);
            existingMaterial.setDeleted(true);
        } catch (Exception e) {
            log.error("Error deleting material", e);
            throw new InternalServerErrorException(Constants.ErrorCode.DELETE_MATERIAL_FAILED, e.getMessage());
        }
    }

    // --- Private helpers ---
    private void validateIdNotNull(Long id, String errorCode) {
        if (id == null) {
            throw new BadRequestException(errorCode);
        }
    }
}
