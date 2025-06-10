package com.genedu.content.service.impl;

import com.genedu.content.dto.schoolclass.SchoolClassRequestDTO;
import com.genedu.content.dto.schoolclass.SchoolClassResponseDTO;
import com.genedu.content.mapper.SchoolClassMapper;
import com.genedu.content.model.SchoolClass;
import com.genedu.content.service.SchoolClassService;
import lombok.RequiredArgsConstructor;
import com.genedu.content.repository.SchoolClassRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SchoolClassServiceImpl implements SchoolClassService {
    private final SchoolClassRepository schoolClassRepository;

//    @Override
//    public List<SchoolClassResponseDTO> getAllSchoolClasses(int pageNo) {
//        List<SchoolClass> schoolClasses = schoolClassRepository.findAll();
//
//        if (schoolClasses.isEmpty()) {
//            return List.of();
//        }
//
//        return schoolClasses.stream()
//                .map(schoolClass -> new SchoolClassResponseDTO(
//                        schoolClass.getId().toString(),
//                        schoolClass.getName(),
//                        schoolClass.getDescription()))
//                .toList();
//    }

    /*
     * Fetches all school classes and converts them to DTOs.
     *
     * @return List of SchoolClassResponseDTO
     */
    @Override
    public List<SchoolClassResponseDTO> getAllSchoolClasses() {
        List<SchoolClass> schoolClasses = schoolClassRepository.findAll();

        if (schoolClasses.isEmpty()) {
            return List.of();
        }

        return schoolClasses.stream()
//                .map(schoolClass -> SchoolClassResponseDTO.fromSchoolClass(schoolClass))
                .map(SchoolClassMapper::toDTO)
                .toList();
    }

    /**
     * Retrieves a SchoolClass by its ID and converts it to a DTO.
     * Throws IllegalArgumentException if the ID is null or not found.
     *
     * @param id ID of the school class.
     * @return SchoolClassResponseDTO containing school class details.
     */
    public SchoolClassResponseDTO getSchoolClassById(Integer id) {
        SchoolClass schoolClass = getSchoolClassEntityById(id);
        return SchoolClassMapper.toDTO(schoolClass);
    }

    /**
     * Retrieves a SchoolClass entity by its ID.
     * Throws IllegalArgumentException if the ID is null or not found.
     *
     * @param id ID of the school class.
     * @return SchoolClass entity.
     */
    @Override
    public SchoolClass getSchoolClassEntityById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("School class ID cannot be null.");
        }

        return schoolClassRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("School class not found with id: " + id));
    }

    /*
     * Creates a new school class from the provided DTO.
     *
     * @param schoolClassRequestDTO DTO containing school class details
     * @return SchoolClassResponseDTO of the created school class
     */
    @Override
    public SchoolClassResponseDTO createSchoolClass(SchoolClassRequestDTO schoolClassRequestDTO) {
        if (schoolClassRepository.existsByName(schoolClassRequestDTO.name())) {
            throw new IllegalArgumentException(
                    "School class with name " + schoolClassRequestDTO.name() + " already exists."
            );
        }

        SchoolClass schoolClass = SchoolClassMapper.toEntity(schoolClassRequestDTO);
        schoolClass = schoolClassRepository.save(schoolClass);

        return SchoolClassMapper.toDTO(schoolClass);
    }

    /*
     * Updates an existing school class with the provided DTO.
     *
     * @param id ID of the school class to update
     * @param schoolClassRequestDTO DTO containing updated school class details
     * @return SchoolClassResponseDTO of the updated school class
     */
    @Override
    public SchoolClassResponseDTO updateSchoolClass(Integer id, SchoolClassRequestDTO schoolClassRequestDTO) {
        SchoolClass existingSchoolClass = schoolClassRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("School class with ID " + id + " does not exist."));

        if (schoolClassRepository.existsByNameAndIdNot(schoolClassRequestDTO.name(), id)) {
            throw new IllegalArgumentException(
                    "School class with name " + schoolClassRequestDTO.name() + " already exists.");
        }

        existingSchoolClass = SchoolClassMapper.toEntity(schoolClassRequestDTO);

        existingSchoolClass = schoolClassRepository.save(existingSchoolClass);
        return SchoolClassMapper.toDTO(existingSchoolClass);
    }

    /*
     * Deletes a school class by its ID.
     *
     * @param id ID of the school class to delete
     */
    public void deleteSchoolClass(Integer id) {
        if (!schoolClassRepository.existsById(id)) {
            throw new IllegalArgumentException("School class with ID " + id + " does not exist.");
        }
        schoolClassRepository.deleteById(id);
    }


}
