package com.genedu.lecturecontent.service;

import com.genedu.commonlibrary.webclient.dto.LessonPlanFileDownloadDTO;
import com.genedu.lecturecontent.dto.LessonEntityResponseDTO;
import com.genedu.lecturecontent.webclient.ContentWebClientService;
import com.genedu.lecturecontent.webclient.LectureMediaWebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class SlideContentService {
    private final LectureMediaWebClientService lectureMediaWebClientService;
    private final ContentWebClientService contentWebClientService;

    private final Logger logger = Logger.getLogger(SlideContentService.class.getName());


    public String getLessonPlanContent(Long lessonPlanFileId) {
        if (lessonPlanFileId == null) {
            return null;
        }
        LessonPlanFileDownloadDTO lessonPlanFileDownloadDTO = lectureMediaWebClientService.getLessonPlanFileByLessonPlanId(lessonPlanFileId);
        String lessonPlanContent = lessonPlanFileDownloadDTO.getContent();

        logger.info("Retrieved lesson plan content: " + lessonPlanContent);

        return lessonPlanContent != null ? lessonPlanContent : "";
    }

    public String getLessonPlanContentByProjectId(String projectId) {
        return lectureMediaWebClientService.getLessonPlanFileContentByProjectId(projectId).getContent();
    }

    public LessonEntityResponseDTO getLessonEntityById(Long lessonId) {
        if (lessonId == null) {
            return null;
        }
        return contentWebClientService.getLessonByLessonId(lessonId);

    }
}
