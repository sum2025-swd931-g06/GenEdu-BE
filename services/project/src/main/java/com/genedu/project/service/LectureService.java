package com.genedu.project.service;

import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.project.dto.LectureContentRequestDTO;
import com.genedu.project.dto.LectureContentResponseDTO;
import com.genedu.project.mapper.LectureContentMapper;
import com.genedu.project.mapper.SlideContentMapper;
import com.genedu.project.model.LectureContent;
import com.genedu.project.model.SlideContent;
import com.genedu.project.model.enumeration.LectureStatus;
import com.genedu.project.repository.LectureContentRepository;
import com.genedu.project.repository.SlideContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureContentRepository lectureContentRepository;
    private final SlideContentRepository slideContentRepository;
    private final LectureContentMapper lectureContentMapper;
    private final SlideContentMapper slideContentMapper;

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

    public LectureContent getLectureContentEntityById(UUID lectureContentId) {
        Optional<LectureContent> lectureContent = lectureContentRepository.findById(lectureContentId);
        return lectureContent.orElseThrow(() -> new NotFoundException("Lecture content not found for ID: " + lectureContentId));
    }

    @Transactional
    public LectureContentResponseDTO createLectureContent(LectureContentRequestDTO lectureContentRequest) {
        LectureContent lectureContent = lectureContentMapper.toEntity(lectureContentRequest);
        lectureContent.setStatus(LectureStatus.DRAFT);
        LectureContent savedLectureContent = lectureContentRepository.save(lectureContent);
        List<SlideContent> slideContents = new ArrayList<>();
        // Create and associate the slides before the first save
        if (lectureContentRequest.slideContents() != null && !lectureContentRequest.slideContents().isEmpty()) {
            LectureContent finalSavedLectureContent = savedLectureContent;
            List<SlideContent> unSavedSlideContent = lectureContentRequest.slideContents().stream()
                    .map(slideDTO -> {
                        SlideContent slide = SlideContent.builder()
                                .orderNumber(slideDTO.orderNumber())
                                .slideTitle(slideDTO.title())
                                .mainIdea(slideDTO.slideType())
                                .subpoints(slideDTO.subpoints())
                                .lectureContent(finalSavedLectureContent) // Associate with the saved lecture content
                                .narrationScript(slideDTO.narrationScript())
                                .build();
                        return slide;
                    })
                    .toList();
            // Save all slide contents in a single batch
            slideContents = slideContentRepository.saveAll(unSavedSlideContent);
        }
        savedLectureContent.setSlideContents(slideContents);
        // Save the lecture content again to persist the slide associations
        savedLectureContent = lectureContentRepository.save(savedLectureContent);

        return lectureContentMapper.toDTO(savedLectureContent);
    }

    @Transactional // Ensures the find and save happen in the same transaction
    public LectureContentResponseDTO updateLectureContent(UUID lectureContentId, LectureContentRequestDTO lectureContentRequest) {
        // 1. Fetch the existing, managed entity from the database.
        LectureContent existingLectureContent = lectureContentRepository.findById(lectureContentId)
                .orElseThrow(() -> new NotFoundException("Lecture content not found for ID: " + lectureContentId));
        //TODO: Apply update logic for other fields if needed
        // 2. Update the title and version
        return null;
    }
}


