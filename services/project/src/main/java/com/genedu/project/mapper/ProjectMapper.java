package com.genedu.project.mapper;

import com.genedu.project.dto.ProjectRequestDTO;
import com.genedu.project.dto.ProjectResponseDTO;
import com.genedu.project.model.Project;
import com.genedu.project.service.LectureMediaWebClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProjectMapper {
    private final LectureMediaWebClientService lectureMediaWebClientService;

    public ProjectResponseDTO toDTO(Project project) {
        String fileUrl = lectureMediaWebClientService.getLessonPlanFileUrlByLessonPlanId(project.getLessonPlanFileId());
        return new ProjectResponseDTO(
                project.getId(),
                project.getUserId(),
                project.getLessonId(),
                project.getTitle(),
                fileUrl,
                project.getCustomInstructions(),
                project.getStatus(),
                project.getSlideNum(),
                project.getTemplateId()
        );
    }
}
