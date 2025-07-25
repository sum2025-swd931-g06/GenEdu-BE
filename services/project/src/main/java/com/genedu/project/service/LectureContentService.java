package com.genedu.project.service;

import com.genedu.commonlibrary.webclient.dto.SlideFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.SlideFileUploadDTO;
import com.genedu.project.dto.LectureContentRequestDTO;
import com.genedu.project.dto.LectureContentResponseDTO;
import com.genedu.project.model.LectureContent;

import java.util.List;
import java.util.UUID;

public interface LectureContentService {
    public List<LectureContentResponseDTO> getLectureContentByProjectId(UUID projectId);

    public LectureContentResponseDTO getLectureContentById(UUID lectureContentId);

    public LectureContent getLectureContentEntityById(UUID lectureContentId);

    public LectureContentResponseDTO createLectureContent(LectureContentRequestDTO lectureContentRequest);

    public LectureContentResponseDTO updateLectureContent(UUID lectureContentId, LectureContentRequestDTO lectureContentRequest);

    public SlideFileDownloadDTO uploadSlideFile(SlideFileUploadDTO fileUploadDTO) ;

    void generateNarrationForLectureContentAsyn(UUID lectureContentId);

    void updateNarrationAudioForLectureContent(UUID lectureContentId, Long audioFileId);

    public LectureContentResponseDTO generateNarrationForLectureContent(
            UUID lectureContentId
    );

    void generateLectureVideoForLectureContentAsyn(UUID finalizedLectureId);
}
