package com.genedu.project.service;

import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.project.dto.LectureContentRequestDTO;
import com.genedu.project.dto.LectureContentResponseDTO;
import com.genedu.project.mapper.LectureContentMapper;
import com.genedu.project.model.LectureContent;
import com.genedu.project.repository.LectureContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LectureContentService {

    private final LectureContentRepository lectureContentRepository;
    private final LectureContentMapper lectureContentMapper;

    public LectureContentResponseDTO getLectureContentByProjectId(UUID projectId) {
        LectureContent lectureContent = lectureContentRepository.findLastByProjectIdAndDeletedIsFalseOrderByCreatedOnDesc(projectId);
        if (lectureContent == null) {
            throw new NotFoundException("Lecture content not found for project ID: " + projectId);
        }
        return lectureContentMapper.toDTO(lectureContent);
    }

    public LectureContentResponseDTO getLectureContentById(UUID lectureContentId) {
        LectureContent lectureContent = lectureContentRepository.findById(lectureContentId)
                .orElseThrow(() -> new NotFoundException("Lecture content not found for ID: " + lectureContentId));

        return lectureContentMapper.toDTO(lectureContent);
    }

    public LectureContentResponseDTO createLectureContent(LectureContentRequestDTO lectureContentRequest) {
        LectureContent lectureContent = lectureContentMapper.toEntity(lectureContentRequest);
        lectureContentRepository.save(lectureContent);
        return lectureContentMapper.toDTO(lectureContent);
    }

    public LectureContentResponseDTO updateLectureContent(UUID lectureContentId, LectureContentRequestDTO lectureContentRequest) {
        LectureContent existingLectureContent = lectureContentRepository.findById(lectureContentId)
                .orElseThrow(() -> new NotFoundException("Lecture content not found for ID: " + lectureContentId));

        LectureContent updatedLectureContent = lectureContentMapper.toEntity(lectureContentRequest);
        updatedLectureContent.setId(existingLectureContent.getId());

        lectureContentRepository.save(updatedLectureContent);
        return lectureContentMapper.toDTO(updatedLectureContent);
    }
}
