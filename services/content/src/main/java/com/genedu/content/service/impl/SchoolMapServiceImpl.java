package com.genedu.content.service.impl;

import com.genedu.content.dto.flatResponse.*;
import com.genedu.content.model.Lesson;
import com.genedu.content.model.Material;
import com.genedu.content.model.SchoolClass;
import com.genedu.content.model.Subject;
import com.genedu.content.repository.LessonRepository;
import com.genedu.content.repository.MaterialRepository;
import com.genedu.content.repository.SchoolClassRepository;
import com.genedu.content.repository.SubjectRepository;
import com.genedu.content.service.SchoolMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SchoolMapServiceImpl implements SchoolMapService {
    private final SchoolClassRepository schoolClassRepository;
    private final SubjectRepository subjectRepository;
    private final MaterialRepository materialRepository;
    private final LessonRepository lessonRepository;

    @Transactional(readOnly = true)
    @Override
    public SchoolDataMapResponse getAllDataAsMap() {
        // School Classes
        Map<Integer, SchoolClassMapDTO> schoolClasses = schoolClassRepository.findAll()
                .stream()
                .filter(sc -> !sc.isDeleted())
                .collect(Collectors.toMap(
                        SchoolClass::getId,
                        sc -> SchoolClassMapDTO.builder()
                                .schoolClassId(sc.getId())
                                .schoolClassName(sc.getName())
                                .build()
                ));

        // Subjects
        Map<Integer, SubjectMapDTO> subjects = subjectRepository.findAll()
                .stream()
                .filter(s -> !s.isDeleted())
                .collect(Collectors.toMap(
                        Subject::getId,
                        s -> SubjectMapDTO.builder()
                                .subjectId(s.getId())
                                .subjectName(s.getName())
                                .schoolClassId(s.getSchoolClass().getId())
                                .build()
                ));

        // Materials
        Map<Long, MaterialMapDTO> materials = materialRepository.findAll()
                .stream()
                .filter(m -> !m.isDeleted())
                .collect(Collectors.toMap(
                        Material::getId,
                        m -> MaterialMapDTO.builder()
                                .materialId(m.getId())
                                .materialTitle(m.getTitle())
                                .subjectId(m.getSubject().getId())
                                .build()
                ));

        // Lessons
        Map<Long, LessonMapDTO> lessons = lessonRepository.findAll()
                .stream()
                .filter(l -> !l.isDeleted())
                .collect(Collectors.toMap(
                        Lesson::getId,
                        l -> LessonMapDTO.builder()
                                .lessonId(l.getId())
                                .lessonTitle(l.getTitle())
                                .materialId(l.getChapter().getMaterial().getId())
                                .build()
                ));

        return SchoolDataMapResponse.builder()
                .schoolClasses(schoolClasses)
                .subjects(subjects)
                .materials(materials)
                .lessons(lessons)
                .build();
    }
}
