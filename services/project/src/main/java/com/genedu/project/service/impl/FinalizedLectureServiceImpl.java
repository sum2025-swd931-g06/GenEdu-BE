package com.genedu.project.service.impl;

import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.project.dto.FinalizedLectureCreateRequestDTO;
import com.genedu.project.dto.FinalizedLectureResponseDTO;
import com.genedu.project.mapper.FinalizedLectureMapper;
import com.genedu.project.model.FinalizedLecture;
import com.genedu.project.model.LectureContent;
import com.genedu.project.model.enumeration.PublishedStatus;
import com.genedu.project.repository.FinalizedLectureRepository;
import com.genedu.project.service.FinalizedLectureService;
import com.genedu.project.service.LectureContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinalizedLectureServiceImpl implements FinalizedLectureService {
    private final FinalizedLectureRepository finalizedLectureRepository;
    private final FinalizedLectureMapper finalizedLectureMapper;
    private final LectureContentService lectureContentServiceImpl;

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

    public FinalizedLectureResponseDTO createFinalizedLecture(
            FinalizedLectureCreateRequestDTO finalizedLectureCreateRequestDTO
    ) {
        if (finalizedLectureCreateRequestDTO.lectureContentId() == null) {
            throw new NotFoundException("Lecture content ID must be provided to create a finalized lecture.");
        }
        LectureContent lectureContent = lectureContentServiceImpl.getLectureContentEntityById(finalizedLectureCreateRequestDTO.lectureContentId());
        FinalizedLecture finalizedLecture = finalizedLectureMapper.toEntity(finalizedLectureCreateRequestDTO, lectureContent);
        FinalizedLecture savedFinalizedLecture = finalizedLectureRepository.save(finalizedLecture);
        return finalizedLectureMapper.toDTO(savedFinalizedLecture);
    }

    public boolean isFinalizedLectureExists(
            UUID lectureContentId
    ) {
        return !finalizedLectureRepository.findByLectureContentIdAndDeletedIsFalse(lectureContentId).isEmpty();
    }

    public FinalizedLecture createFinalizedLectureEntity(
            FinalizedLectureCreateRequestDTO finalizedLectureCreateRequestDTO
    ) {
        LectureContent lectureContent = lectureContentServiceImpl.getLectureContentEntityById(finalizedLectureCreateRequestDTO.lectureContentId());
        FinalizedLecture finalizedLecture = finalizedLectureMapper.toEntity(finalizedLectureCreateRequestDTO, lectureContent);
        return finalizedLectureRepository.save(finalizedLecture);
    }

    public FinalizedLecture updateFinalizedLectureMedia(
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
        return finalizedLectureRepository.save(existingFinalizedLecture);
    }
}
