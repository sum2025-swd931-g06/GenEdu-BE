package com.genedu.content.service.impl;

import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.DuplicatedException;
import com.genedu.commonlibrary.exception.InternalServerErrorException;
import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.content.dto.flatResponse.FlatSchoolClassSubjectDTO;
import com.genedu.content.dto.schoolclass.SchoolClassResponseDTO;
import com.genedu.content.dto.subject.SubjectRequestDTO;
import com.genedu.content.mapper.SchoolClassMapper;
import com.genedu.content.mapper.SubjectMapper;
import com.genedu.content.model.SchoolClass;
import com.genedu.content.model.Subject;
import com.genedu.content.repository.SubjectRepository;
import com.genedu.content.service.SchoolClassService;
import com.genedu.content.service.SubjectService;
import com.genedu.content.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final SchoolClassService schoolClassService;

    @Transactional(readOnly = true)
    @Override
    public List<FlatSchoolClassSubjectDTO> getAllSubjects() {
        return subjectRepository.findAll()
                .stream()
                .map(SubjectMapper::toFlatDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public FlatSchoolClassSubjectDTO getSubjectById(Integer id) {
        Subject subject = getSubjectEntityById(id);
        return SubjectMapper.toFlatDTO(subject);
    }

    @Transactional(readOnly = true)
    @Override
    public SchoolClassResponseDTO getSubjectsBySchoolClassId(Integer schoolClassId) {
        SchoolClass schoolClass = schoolClassService.getSchoolClassEntityById(schoolClassId);
        var subjects = subjectRepository.findBySchoolClass_Id(schoolClassId)
                .stream()
                .map(SubjectMapper::toDTO)
                .toList();
        return SchoolClassMapper.toDTOWithSubjects(schoolClass, subjects);
    }

    @Transactional(readOnly = true)
    @Override
    public Subject getSubjectEntityById(Integer id) {
        validateIdNotNull(id, Constants.ErrorCode.SUBJECT_ID_REQUIRED);
        return subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.SUBJECT_NOT_FOUND, id));
    }

    @Override
    public FlatSchoolClassSubjectDTO createSubject(Integer schoolClassId, SubjectRequestDTO subjectRequestDTO) {
        if (subjectRepository.existsByName(subjectRequestDTO.name())) {
            throw new DuplicatedException(Constants.ErrorCode.DUPLICATED_SUBJECT_NAME, subjectRequestDTO.name());
        }
        if (schoolClassId == null) {
            throw new BadRequestException(Constants.ErrorCode.SCHOOL_CLASS_ID_REQUIRED);
        }
        SchoolClass schoolClass = schoolClassService.getSchoolClassEntityById(schoolClassId);

        Subject createdSubject = SubjectMapper.toEntity(subjectRequestDTO, schoolClass);

        try {
            Subject saveSubject = subjectRepository.save(createdSubject);
            return SubjectMapper.toFlatDTO(saveSubject);
        } catch (Exception e) {
            log.error("Error creating material", e);
            throw new InternalServerErrorException(Constants.ErrorCode.CREATE_SUBJECT_FAILED, e.getMessage());
        }
    }

    @Override
    public FlatSchoolClassSubjectDTO updateSubject(Integer id, SubjectRequestDTO subjectRequestDTO) {
        Subject existingSubject = getSubjectEntityById(id);

        if (subjectRepository.existsByNameAndIdNot(
                subjectRequestDTO.name(),
                id)
        ) {
            throw new DuplicatedException(Constants.ErrorCode.DUPLICATED_SUBJECT_NAME, subjectRequestDTO.name());
        }

        try {
            existingSubject.setName(subjectRequestDTO.name());
            existingSubject.setDescription(subjectRequestDTO.description());

            Subject updatedSubject = subjectRepository.save(existingSubject);
            return SubjectMapper.toFlatDTO(updatedSubject);
        } catch (Exception e) {
            log.error("Error updating material", e);
            throw new InternalServerErrorException(Constants.ErrorCode.UPDATE_SUBJECT_FAILED, e.getMessage());
        }
    }

    @Override
    public void deleteSubject(Integer id) {
        validateIdNotNull(id, Constants.ErrorCode.SUBJECT_ID_REQUIRED);

        if (!subjectRepository.existsById(id)) {
            throw new NotFoundException(Constants.ErrorCode.SUBJECT_NOT_FOUND, id);
        }

        try {
            var existingSubject = getSubjectEntityById(id);
            existingSubject.setDeleted(true);
        } catch (Exception e) {
            log.error("Error deleting subject", e);
            throw new InternalServerErrorException(Constants.ErrorCode.DELETE_SUBJECT_FAILED, e.getMessage());
        }
    }

    private void validateIdNotNull(Integer id, String errorCode) {
        if (id == null) {
            throw new BadRequestException(errorCode);
        }
    }
}