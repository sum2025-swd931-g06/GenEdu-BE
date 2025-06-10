package com.genedu.content.service.impl;

import com.genedu.content.dto.subject.SubjectRequestDTO;
import com.genedu.content.dto.subject.SubjectResponseDTO;
import com.genedu.content.mapper.SubjectMapper;
import com.genedu.content.model.SchoolClass;
import com.genedu.content.model.Subject;
import com.genedu.content.repository.SubjectRepository;
import com.genedu.content.service.SchoolClassService;
import com.genedu.content.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final SchoolClassService schoolClassService;

    /**
     * Retrieves all subjects from the database and maps them to DTOs.
     *
     * @return a list of SubjectResponseDTO representing all subjects.
     */
    @Override
    public List<SubjectResponseDTO> getAllSubjects() {
        return subjectRepository.findAll()
                .stream()
                .map(SubjectMapper::toDTO)
                .toList();
    }

    /**
     * Retrieves a subject by its ID and maps it to a DTO.
     *
     * @param id the ID of the subject.
     * @return the SubjectResponseDTO representing the subject.
     * @throws IllegalArgumentException if the ID is null or the subject is not found.
     */
    @Override
    public SubjectResponseDTO getSubjectById(Long id) {
        return SubjectMapper.toDTO(getSubjectEntityById(id));
    }

    @Override
    public List<SubjectResponseDTO> getSubjectsBySchoolClassId(Integer schoolClassId) {
        if (schoolClassId == null) {
            throw new IllegalArgumentException("School class ID cannot be null.");
        }
        return subjectRepository.findBySchoolClass_Id(schoolClassId)
                .stream()
                .map(SubjectMapper::toDTO)
                .toList();
    }

    /**
     * Retrieves the Subject entity by its ID.
     *
     * @param id the ID of the subject.
     * @return the Subject entity.
     * @throws IllegalArgumentException if the ID is null or the subject is not found.
     */
    @Override
    public Subject getSubjectEntityById(Long id) {
        validateIdNotNull(id, "Subject");

        return subjectRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Subject not found with id: " + id));
    }

    /**
     * Creates a new subject based on the given request DTO.
     *
     * @param subjectRequestDTO the SubjectRequestDTO containing subject details.
     * @return the SubjectResponseDTO of the created subject.
     * @throws IllegalArgumentException if a subject with the same name already exists.
     */
    @Override
    public SubjectResponseDTO createSubject(Integer schoolClassId, SubjectRequestDTO subjectRequestDTO) {
        if (subjectRepository.existsByName(subjectRequestDTO.name())) {
            throw new IllegalArgumentException("Subject with name '" + subjectRequestDTO.name() + "' already exists.");
        }

        if (schoolClassId == null) {
            throw new IllegalArgumentException("School class ID cannot be null.");
        }
        SchoolClass schoolClass = schoolClassService.getSchoolClassEntityById(schoolClassId);

        Subject createdSubject = SubjectMapper.toEntity(subjectRequestDTO, schoolClass);

        Subject savedSubject = subjectRepository.save(createdSubject);
        return SubjectMapper.toDTO(savedSubject);
    }

    /**
     * Updates an existing subject by its ID using the provided DTO.
     *
     * @param id  the ID of the subject to update.
     * @param subjectRequestDTO the DTO containing updated subject information.
     * @return the updated SubjectResponseDTO.
     * @throws IllegalArgumentException if the subject is not found or the new name already exists on a different subject.
     */
    @Override
    public SubjectResponseDTO updateSubject(Long id, SubjectRequestDTO subjectRequestDTO) {
        Subject existingSubject = getSubjectEntityById(id);

        if (subjectRepository.existsByNameAndIdNot(subjectRequestDTO.name(), id)) {
            throw new IllegalArgumentException("Subject with name '" + subjectRequestDTO.name() + "' already exists.");
        }

        existingSubject.setName(subjectRequestDTO.name());
        existingSubject.setDescription(subjectRequestDTO.description());

        Subject updatedSubject = subjectRepository.save(existingSubject);
        return SubjectMapper.toDTO(updatedSubject);
    }

    /**
     * Deletes a subject by its ID.
     *
     * @param id the ID of the subject to delete.
     * @throws IllegalArgumentException if the ID is null or the subject does not exist.
     */
    @Override
    public void deleteSubject(Long id) {
        validateIdNotNull(id, "Subject");

        if (!subjectRepository.existsById(id)) {
            throw new IllegalArgumentException("Subject not found with id: " + id);
        }

        subjectRepository.deleteById(id);
    }

    /**
     * Validates that the given ID is not null.
     *
     * @param id         the ID to validate.
     * @param entityName the name of the entity (for error message clarity).
     * @throws IllegalArgumentException if the ID is null.
     */
    private void validateIdNotNull(Long id, String entityName) {
        if (id == null) {
            throw new IllegalArgumentException(entityName + " ID cannot be null.");
        }
    }
}
