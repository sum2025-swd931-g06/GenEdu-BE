package com.genedu.project.service.impl;

import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.commonlibrary.kafka.dto.SlideNarrationEvent;
import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.project.dto.FinalizedLectureCreateRequestDTO;
import com.genedu.project.dto.FinalizedLectureResponseDTO;
import com.genedu.project.kafka.KafkaProducer;
import com.genedu.project.mapper.FinalizedLectureMapper;
import com.genedu.project.model.FinalizedLecture;
import com.genedu.project.model.LectureContent;
import com.genedu.project.model.Project;
import com.genedu.project.model.SlideContent;
import com.genedu.project.model.enumeration.LectureStatus;
import com.genedu.project.model.enumeration.PublishedStatus;
import com.genedu.project.repository.FinalizedLectureRepository;
import com.genedu.project.repository.LectureContentRepository;
import com.genedu.project.repository.ProjectRepository;
import com.genedu.project.repository.SlideContentRepository;
import com.genedu.project.service.FinalizedLectureService;
import com.genedu.project.service.LectureContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinalizedLectureServiceImpl implements FinalizedLectureService {
    private final SlideContentRepository slideContentRepository;
    private final FinalizedLectureRepository finalizedLectureRepository;
    private final FinalizedLectureMapper finalizedLectureMapper;
    private final LectureContentService lectureContentServiceImpl;
    private final LectureContentRepository lectureContentRepository;
    private final KafkaProducer kafkaProducer;

    public List<FinalizedLectureResponseDTO> getFinalizedLecturesByLectureContentId(
            UUID lectureContentId
    ) {
        return finalizedLectureRepository.findByLectureContentIdAndDeletedIsFalse(lectureContentId)
                .stream()
                .map(finalizedLectureMapper::toDTO)
                .toList();
    }

    @Override
    public List<FinalizedLecture> getFinalizedLectureEntitiesByLectureContentId(UUID lectureContentId) {
        return finalizedLectureRepository.findByLectureContentIdAndDeletedIsFalse(lectureContentId);
    }

    public  FinalizedLectureResponseDTO getFinalizedLectureById(
            UUID finalizedLectureId
    ) {
        return finalizedLectureRepository.findById(finalizedLectureId)
                .map(finalizedLectureMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Finalized lecture not found for ID: " + finalizedLectureId));
    }

    @Transactional
    public FinalizedLectureResponseDTO createFinalizedLecture(
            FinalizedLectureCreateRequestDTO finalizedLectureCreateRequestDTO
    ) {
        if (finalizedLectureCreateRequestDTO.lectureContentId() == null) {
            throw new NotFoundException("Lecture content ID must be provided to create a finalized lecture.");
        }
        LectureContent lectureContent = lectureContentServiceImpl.getLectureContentEntityById(finalizedLectureCreateRequestDTO.lectureContentId());
        FinalizedLecture finalizedLecture = finalizedLectureMapper.toEntity(finalizedLectureCreateRequestDTO, lectureContent);
        FinalizedLecture savedFinalizedLecture = finalizedLectureRepository.save(finalizedLecture);
        lectureContent.setFinalizedLecture(savedFinalizedLecture);
        lectureContent.setStatus(LectureStatus.FINALIZED);
        return finalizedLectureMapper.toDTO(savedFinalizedLecture);
    }

    public boolean isFinalizedLectureExists(
            UUID lectureContentId
    ) {
        return !finalizedLectureRepository.findByLectureContentIdAndDeletedIsFalse(lectureContentId).isEmpty();
    }

    @Override
    public void updateNarrationAudioForLectureContent(UUID finalizedLectureId, Long lectureVideoId) {
        FinalizedLecture finalizedLecture = finalizedLectureRepository.findById(finalizedLectureId)
                .orElseThrow(() -> new NotFoundException("Finalized lecture not found for ID: " + finalizedLectureId));

        finalizedLecture.setVideoFileId(lectureVideoId);
        finalizedLectureRepository.save(finalizedLecture);
    }

    @Override
    public void generatedLectureVideo(UUID finalizedLectureId) {
        FinalizedLecture finalizedLecture = finalizedLectureRepository.findByIdAndDeletedIsFalse(finalizedLectureId);
        UUID lectureContentId = finalizedLecture.getLectureContent().getId();
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
                .finalizeLectureId(finalizedLecture.getId())
                .slideFileId(finalizedLecture.getPresentationFileId())
                .slideNarrations(slideNarrations)
                .jwtToken(AuthenticationUtils.extractJwt())
                .build();

        log.info("Generating narration for lecture content ID: {}", lectureContentId);

        kafkaProducer.sendSlideNarrationEvent(event);
    }

    @Override
    public List<FinalizedLectureResponseDTO> getFinalizedLectureByProjectId(UUID projectId) {
        List<LectureContent> lectureContents = lectureContentRepository.findByProjectIdAndDeletedIsFalse(projectId);
        return lectureContents.stream()
                .filter(lectureContent -> lectureContent.getFinalizedLecture() != null)
                .map(lectureContent -> finalizedLectureMapper.toDTO(lectureContent.getFinalizedLecture()))
                .toList();
    }

    public FinalizedLecture createFinalizedLectureEntity(
            FinalizedLectureCreateRequestDTO finalizedLectureCreateRequestDTO
    ) {
        LectureContent lectureContent = lectureContentServiceImpl.getLectureContentEntityById(finalizedLectureCreateRequestDTO.lectureContentId());
        FinalizedLecture finalizedLecture = finalizedLectureMapper.toEntity(finalizedLectureCreateRequestDTO, lectureContent);
        return finalizedLectureRepository.save(finalizedLecture);
    }

    @Transactional
    public FinalizedLectureResponseDTO updateFinalizedLectureMedia(
            UUID finalizedLectureId,
            FinalizedLectureCreateRequestDTO finalizedLectureCreateRequestDTO
    ) {
        FinalizedLecture existingFinalizedLecture = finalizedLectureRepository.findById(finalizedLectureId)
                .orElseThrow(() -> new NotFoundException("Finalized lecture not found for ID: " + finalizedLectureId));

        if (finalizedLectureCreateRequestDTO.audioFileId() != null) {
            existingFinalizedLecture.setAudioFileId(finalizedLectureCreateRequestDTO.audioFileId());
        }
        if (finalizedLectureCreateRequestDTO.presentationFileId() != null) {
            existingFinalizedLecture.setPresentationFileId(finalizedLectureCreateRequestDTO.presentationFileId());
        }
        if (finalizedLectureCreateRequestDTO.videoFileId() != null) {
            existingFinalizedLecture.setVideoFileId(finalizedLectureCreateRequestDTO.videoFileId());
        }
        if (finalizedLectureCreateRequestDTO.thumbnailFileId() != null) {
            existingFinalizedLecture.setThumbnailFileId(finalizedLectureCreateRequestDTO.thumbnailFileId());
        }

        FinalizedLecture savedFinalizeLecture = finalizedLectureRepository.save(existingFinalizedLecture);

        return finalizedLectureMapper.toDTO(savedFinalizeLecture);
    }
}
