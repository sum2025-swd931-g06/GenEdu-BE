package com.genedu.project.service.impl;

import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.commonlibrary.kafka.dto.LectureVideoGenerateEvent;
import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.commonlibrary.webclient.dto.SlideFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.SlideFileUploadDTO;
import com.genedu.commonlibrary.kafka.dto.SlideNarrationEvent;
import com.genedu.project.dto.LectureContentRequestDTO;
import com.genedu.project.dto.LectureContentResponseDTO;
import com.genedu.project.kafka.KafkaProducer;
import com.genedu.project.mapper.LectureContentMapper;
import com.genedu.project.model.FinalizedLecture;
import com.genedu.project.model.LectureContent;
import com.genedu.project.model.SlideContent;
import com.genedu.project.model.enumeration.LectureStatus;
import com.genedu.project.repository.FinalizedLectureRepository;
import com.genedu.project.repository.LectureContentRepository;
import com.genedu.project.repository.SlideContentRepository;
import com.genedu.project.service.LectureContentService;
import com.genedu.project.webclient.LectureMediaWebClientService;
import com.genedu.project.webclient.ProjectMediaWebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureContentServiceImpl implements LectureContentService {

    private final ProjectMediaWebClientService projectMediaWebClientService;
    private final LectureContentRepository lectureContentRepository;
    private final SlideContentRepository slideContentRepository;
    private final FinalizedLectureRepository finalizedLectureRepository;
    private final LectureContentMapper lectureContentMapper;
    private final KafkaProducer kafkaProducer;

    public List<LectureContentResponseDTO> getLectureContentByProjectId(UUID projectId) {
        List<LectureContent> lectureContents = lectureContentRepository.findByProjectIdAndDeletedIsFalse(projectId);
        if (lectureContents.isEmpty()) {
            throw new NotFoundException("Lecture content not found for project ID: " + projectId);
        }
        return lectureContentMapper.toDTOs(lectureContents);
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
        savedLectureContent.setCreatedOn(savedLectureContent.getCreatedOn());
        List<SlideContent> slideContents = new ArrayList<>();
        // Create and associate the slides before the first save
        if (lectureContentRequest.slideContents() != null && !lectureContentRequest.slideContents().isEmpty()) {
            LectureContent finalSavedLectureContent = savedLectureContent;
            List<SlideContent> unSavedSlideContent = lectureContentRequest.slideContents().stream()
                    .map(slideDTO -> {
                        SlideContent slide = SlideContent.builder()
                                .orderNumber(slideDTO.orderNumber())
                                .slideTitle(slideDTO.title())
                                .slideType(slideDTO.slideType())
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
        // 2. Update the fields of the managed entity.
        return null;
    }

    @Transactional
    public SlideFileDownloadDTO uploadSlideFile(SlideFileUploadDTO fileUploadDTO) {
        UUID lectureContentId = UUID.fromString(fileUploadDTO.getLectureContentId());
        LectureContent existingLectureContent = lectureContentRepository.findById(lectureContentId)
                .orElseThrow(() -> new NotFoundException("Lecture content not found for ID: " + fileUploadDTO.getLectureContentId()));
        // Ensure the project ID matches the lecture content's project ID
        SlideFileDownloadDTO slideFileDownloadDTO = projectMediaWebClientService.uploadSlideFile(fileUploadDTO);

        // Check if the upload was successful
        if (slideFileDownloadDTO == null) {
            throw new NotFoundException("Failed to upload slide file");
        }
        return slideFileDownloadDTO;
    }

    @Override
    public void generateNarrationForLectureContentAsyn(UUID lectureContentId) {
        LectureContent lectureContent = lectureContentRepository.findById(lectureContentId)
                .orElseThrow(() -> new NotFoundException("Lecture content not found for ID: " + lectureContentId));

        // Check if the lecture content is finalized
        if (lectureContent.getStatus() != LectureStatus.FINALIZED) {
            throw new IllegalStateException("Lecture content must be finalized before generating narration");
        }

        // Prepare the slide narration event
        List<SlideContent> slideContents = slideContentRepository.findByLectureContentId(lectureContentId);
        List<SlideNarrationEvent.SlideNarration> slideNarrations = slideContents.stream()
                .map(slide -> SlideNarrationEvent.SlideNarration.builder()
                        .slideId(slide.getId())
                        .narrationScript(slide.getNarrationScript())
                        .orderNumber(slide.getOrderNumber())
                        .build())
                .toList();

        SlideNarrationEvent event = SlideNarrationEvent.builder()
                .projectId(lectureContent.getProject().getId())
                .lectureContentId(lectureContentId)
                .slideNarrations(slideNarrations)
                .jwtToken(AuthenticationUtils.extractJwt())
                .build();

        log.info("Generating narration for lecture content ID: {}", lectureContentId);

        kafkaProducer.sendSlideNarrationEvent(event);
    }

    @Override
    public void updateNarrationAudioForLectureContent(UUID lectureContentId, Long audioFileId) {
        SlideContent slideContent = slideContentRepository.findById(lectureContentId)
                .orElseThrow(() -> new NotFoundException("Slide content not found for ID: " + lectureContentId));
        // Update the narration audio file ID
        slideContent.setNarrationFileId(audioFileId);
        slideContentRepository.save(slideContent);
        log.info("Updated narration audio for lecture content ID: {}", lectureContentId);
    }

    @Override
    public LectureContentResponseDTO generateNarrationForLectureContent(UUID lectureContentId) {
        LectureContent lectureContent = lectureContentRepository.findById(lectureContentId)
                .orElseThrow(() -> new NotFoundException("Lecture content not found for ID: " + lectureContentId));
        if (lectureContent.getStatus() != LectureStatus.FINALIZED) {
            throw new IllegalStateException("Lecture content must be finalized before generating narration");
        }
        // Prepare the slide narration event
        List<SlideContent> slideContents = slideContentRepository.findByLectureContentId(lectureContentId);
        List<SlideNarrationEvent.SlideNarration> slideNarrations = slideContents.stream()
                .map(slide -> SlideNarrationEvent.SlideNarration.builder()
                        .slideId(slide.getId())
                        .narrationScript(slide.getNarrationScript())
                        .build())
                .toList();
        return null;
    }

    @Override
    public void generateLectureVideoForLectureContentAsyn(UUID finalizedLectureId) {
        FinalizedLecture finalizedLecture = finalizedLectureRepository.findById(finalizedLectureId)
                .orElseThrow(() -> new NotFoundException("Finalized lecture not found for ID: " + finalizedLectureId));

        UUID lectureContentId = finalizedLecture.getLectureContent().getId();
        LectureContent lectureContent = lectureContentRepository.findById(lectureContentId)
                .orElseThrow(() -> new NotFoundException("Lecture content not found for ID: " + lectureContentId));

        // Check if the lecture content is finalized
        if (lectureContent.getStatus() != LectureStatus.FINALIZED) {
            throw new IllegalStateException("Lecture content must be finalized before generating video");
        }

        // Prepare the lecture video generate event
        List<SlideContent> slideContents = slideContentRepository.findByLectureContentId(lectureContentId);

        Map<Integer, Long> slideNarrationAudios = slideContents.stream()
                .filter(slide -> slide.getNarrationFileId() != null && slide.getOrderNumber() != null && slide.getOrderNumber() > 0)
                .collect(Collectors.toMap(
                        slide -> slide.getOrderNumber() - 1, // Convert to 0-based index
                        SlideContent::getNarrationFileId,
                        (existingValue, newValue) -> newValue // In case of duplicate order numbers, take the last one
                ));

        LectureVideoGenerateEvent event = LectureVideoGenerateEvent.builder()
                .projectId(lectureContent.getProject().getId())
                .lectureContentId(lectureContentId)
                .slideNarrationAudios(slideNarrationAudios)
                .finalizeLectureId(finalizedLecture.getId())
                .slideFileId(finalizedLecture.getPresentationFileId())
                .jwtToken(AuthenticationUtils.extractJwt())
                .build();

        log.info("Generating video for lecture content ID: {}", lectureContentId);
        kafkaProducer.sendLectureVideoGenerateEvent(event);
    }
}


