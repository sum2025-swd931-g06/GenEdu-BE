package com.genedu.content.service.impl;

import com.genedu.content.dto.SchoolClassResponseDTO;
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

    @Override
    public List<SchoolClassResponseDTO> getAllSchoolClasses(int pageNo) {
        List<SchoolClass> schoolClasses = schoolClassRepository.findAll();

        if (schoolClasses.isEmpty()) {
            return List.of();
        }

        return schoolClasses.stream()
                .map(schoolClass -> new SchoolClassResponseDTO(
                        schoolClass.getId().toString(),
                        schoolClass.getName(),
                        schoolClass.getDescription()))
                .toList();
    }
}
