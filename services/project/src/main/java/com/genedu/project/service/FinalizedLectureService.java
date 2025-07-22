package com.genedu.project.service;

import com.genedu.project.dto.FinalizedLectureCreateRequestDTO;
import com.genedu.project.dto.FinalizedLectureResponseDTO;
import com.genedu.project.model.FinalizedLecture;

import java.util.List;
import java.util.UUID;

public interface FinalizedLectureService {
    public List<FinalizedLectureResponseDTO> getFinalizedLecturesByLectureContentId(UUID lectureContentId);

    public List<FinalizedLecture> getFinalizedLectureEntitiesByLectureContentId(UUID lectureContentId);

    public FinalizedLectureResponseDTO getFinalizedLectureById(UUID finalizedLectureId);

    public FinalizedLectureResponseDTO createFinalizedLecture(FinalizedLectureCreateRequestDTO finalizedLectureCreateRequestDTO);

    public FinalizedLecture createFinalizedLectureEntity(FinalizedLectureCreateRequestDTO finalizedLectureCreateRequestDTO);

    public FinalizedLectureResponseDTO updateFinalizedLectureMedia(UUID finalizedLectureId, FinalizedLectureCreateRequestDTO finalizedLectureCreateRequestDTO);

    public boolean isFinalizedLectureExists(UUID finalizedLectureId);

    void updateNarrationAudioForLectureContent(UUID finalizedLectureId, Long lectureVideoId);
}
