package com.genedu.content.service.impl;

import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.DuplicatedException;
import com.genedu.commonlibrary.exception.InternalServerErrorException;
import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.content.dto.schoolclass.SchoolClassRequestDTO;
import com.genedu.content.dto.schoolclass.SchoolClassResponseDTO;
import com.genedu.content.mapper.SchoolClassMapper;
import com.genedu.content.model.SchoolClass;
import com.genedu.content.repository.SchoolClassRepository;
import com.genedu.content.service.SchoolClassService;
import com.genedu.content.utils.Constants;
import com.genedu.content.utils.TextNormalizerUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class SchoolClassServiceImpl implements SchoolClassService {
    private final SchoolClassRepository schoolClassRepository;

    @Transactional(readOnly = true)
    @Override
    public List<SchoolClassResponseDTO> getAllSchoolClasses() {
        List<SchoolClass> schoolClasses = schoolClassRepository.findAll();
        return schoolClasses.stream()
                .map(SchoolClassMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public SchoolClassResponseDTO getSchoolClassById(Integer id) {
        SchoolClass schoolClass = getSchoolClassEntityById(id);
        return SchoolClassMapper.toDTO(schoolClass);
    }

    @Transactional(readOnly = true)
    @Override
    public SchoolClass getSchoolClassEntityById(Integer id) {
        validateIdNotNull(id);
        return schoolClassRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.SCHOOL_CLASS_NOT_FOUND, id));
    }

    @Override
    public SchoolClassResponseDTO createSchoolClass(SchoolClassRequestDTO schoolClassRequestDTO) {
        String normalizedName = TextNormalizerUtils.normalizeTextUsingNormalizer(schoolClassRequestDTO.name());
        if (schoolClassRepository.existsByName(normalizedName)) {
            throw new DuplicatedException(Constants.ErrorCode.DUPLICATED_SCHOOL_CLASS_NAME, schoolClassRequestDTO.name());
        }
        SchoolClass schoolClass = SchoolClassMapper.toEntity(schoolClassRequestDTO);
        try {
            SchoolClass savedSchoolClass = schoolClassRepository.save(schoolClass);
            return SchoolClassMapper.toDTO(savedSchoolClass);
        } catch (Exception e) {
            log.error("Error creating school class", e);
            throw new InternalServerErrorException(Constants.ErrorCode.CREATE_SCHOOL_CLASS_FAILED, e.getMessage());
        }
    }

    @Override
    public SchoolClassResponseDTO updateSchoolClass(Integer id, SchoolClassRequestDTO schoolClassRequestDTO) {
        SchoolClass existingSchoolClass = getSchoolClassEntityById(id);

        String normalizedName = TextNormalizerUtils.normalizeTextUsingNormalizer(schoolClassRequestDTO.name());
        if (schoolClassRepository.existsByNameAndIdNot(normalizedName, id)) {
            throw new DuplicatedException(Constants.ErrorCode.DUPLICATED_SCHOOL_CLASS_NAME, schoolClassRequestDTO.name());
        }

        existingSchoolClass.setName(schoolClassRequestDTO.name());
        existingSchoolClass.setDescription(schoolClassRequestDTO.description());

        try {
            SchoolClass updatedSchoolClass = schoolClassRepository.save(existingSchoolClass);
            return SchoolClassMapper.toDTO(updatedSchoolClass);
        } catch (Exception e) {
            log.error("Error updating school class", e);
            throw new InternalServerErrorException(Constants.ErrorCode.UPDATE_SCHOOL_CLASS_FAILED, e.getMessage());
        }
    }

    @Override
    public void deleteSchoolClass(Integer id) {
        validateIdNotNull(id);
        if (!schoolClassRepository.existsById(id)) {
            throw new NotFoundException(Constants.ErrorCode.SCHOOL_CLASS_NOT_FOUND, id);
        }
        try {
            schoolClassRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error deleting school class", e);
            throw new InternalServerErrorException(Constants.ErrorCode.DELETE_SCHOOL_CLASS_FAILED, e.getMessage());
        }
    }

    private void validateIdNotNull(Integer id) {
        if (id == null) {
            throw new BadRequestException(Constants.ErrorCode.SCHOOL_CLASS_ID_REQUIRED);
        }
    }
}