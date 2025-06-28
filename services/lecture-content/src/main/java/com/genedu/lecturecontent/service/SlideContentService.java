package com.genedu.lecturecontent.service;

import com.genedu.lecturecontent.dto.LectureContentRequestDTO;
import com.genedu.lecturecontent.dto.ProjectResponseDTO;
import com.genedu.lecturecontent.webclient.ContentWebClientService;
import com.genedu.lecturecontent.webclient.LectureMediaWebClientService;
import com.genedu.lecturecontent.webclient.ProjectWebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class SlideContentService {
//    private final ProjectWebClientService projectWebClientService;
//    private final ContentWebClientService contentWebClientService;
//    private final LectureMediaWebClientService lectureMediaWebClientService;
//
//    private Logger logger = Logger.getLogger(SlideContentService.class.getName());
//
//    public ProjectResponseDTO getProjectById(UUID projectId) {
//        if (projectId == null) {
//            return null;
//        }
//        ProjectResponseDTO projectResponseDTO = projectWebClientService.getProjectById(projectId).block();
//        if (projectResponseDTO == null) {
//            logger.warning("Project with ID " + projectId + " not found.");
//            return null;
//        }
//
//        logger.info("Retrieved project: " + projectResponseDTO);
//        return projectResponseDTO;
//    }
////
////    private List<LessonContentResponseDto> getLessonContentsByProjectId(UUID projectId) {
////
////    }
//
//    private String getLessonPlanContent(Long lessonPlanFileId) {
//        if (lessonPlanFileId == null) {
//            return null;
//        }
//        LessonPlanFileDownloadDTO lessonPlanFileDownloadDTO = lectureMediaWebClientService.getLessonPlanFileUrlByLessonPlanId(lessonPlanFileId);
//        String lessonPlanContent = lessonPlanFileDownloadDTO.getContent();
//
//        logger.info("Retrieved lesson plan content: " + lessonPlanContent);
//
//        return lessonPlanContent != null ? lessonPlanContent : "";
//    }

}
