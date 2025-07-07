package com.genedu.content.service;

import com.genedu.content.dto.flatResponse.FlatLessonContentLessonContentMediaFile;
import com.genedu.content.dto.lessoncontent.LessonContentResponseDTO;
import com.genedu.content.dto.lessoncontentmediafile.LessonContentMediaFileRequestDTO;
import com.genedu.content.model.LessonContentMediaFile;

import java.util.List;

public interface LessonContentMediaFileService {
    List<FlatLessonContentLessonContentMediaFile> getAllLessonContentMediaFiles();
    LessonContentMediaFile getLessonContentMediaFileEntityById(Long id);
    FlatLessonContentLessonContentMediaFile getLessonContentMediaFileById(Long id);
    LessonContentResponseDTO getLessonContentMediaFilesByLessonContentId(Long lessonContentId);

    FlatLessonContentLessonContentMediaFile createLessonContentMediaFile(Long lessonContentId, LessonContentMediaFileRequestDTO lessonContentMediaFileRequestDTO);
    FlatLessonContentLessonContentMediaFile updateLessonContentMediaFile(Long id, LessonContentMediaFileRequestDTO lessonContentMediaFileRequestDTO);
    void deleteLessonContentMediaFile(Long id);
}
