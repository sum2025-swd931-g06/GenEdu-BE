package com.genedu.project.service;

import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.commonlibrary.webclient.dto.SlideFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.SlideFileUploadDTO;
import com.genedu.project.dto.FinalizedLectureCreateRequestDTO;
import com.genedu.project.dto.LectureContentRequestDTO;
import com.genedu.project.dto.LectureContentResponseDTO;
import com.genedu.project.model.FinalizedLecture;
import com.genedu.project.model.LectureContent;
import com.genedu.project.model.SlideContent;
import com.genedu.project.model.enumeration.LectureStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LectureContentService {
    public List<LectureContentResponseDTO> getLectureContentByProjectId(UUID projectId);

    public LectureContentResponseDTO getLectureContentById(UUID lectureContentId);

    public LectureContent getLectureContentEntityById(UUID lectureContentId);

    public LectureContentResponseDTO createLectureContent(LectureContentRequestDTO lectureContentRequest);

    public LectureContentResponseDTO updateLectureContent(UUID lectureContentId, LectureContentRequestDTO lectureContentRequest);

    public SlideFileDownloadDTO uploadSlideFile(SlideFileUploadDTO fileUploadDTO) ;
}
