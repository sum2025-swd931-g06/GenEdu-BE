package com.genedu.lecturecontent.service;

import com.genedu.commonlibrary.webclient.dto.LessonPlanFileDownloadDTO;
import com.genedu.lecturecontent.dto.LessonEntityResponseDTO;
import com.genedu.lecturecontent.dto.ProjectResponseDTO;
import com.genedu.lecturecontent.webclient.ContentWebClientService;
import com.genedu.lecturecontent.webclient.LectureMediaWebClientService;
import com.genedu.lecturecontent.webclient.ProjectWebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SlideContentService {
    private final LectureMediaWebClientService lectureMediaWebClientService;
    private final ContentWebClientService contentWebClientService;
    private final ProjectWebClientService projectWebClientService;

    public String getLessonPlanContentByProjectId(String projectId) {
        return lectureMediaWebClientService.getLessonPlanFileContentByProjectId(projectId).getContent();
    }

    public ProjectResponseDTO getProjectByProjectId(UUID projectId) {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID must not be null");
        }
        return projectWebClientService.getProjectByProjectID(projectId);
    }

    public LessonEntityResponseDTO getLessonByLessonId(Long lessonId) {
        if (lessonId == null) {
            throw new IllegalArgumentException("Lesson ID must not be null");
        }
        return contentWebClientService.getLessonByLessonId(lessonId);
    }
}
