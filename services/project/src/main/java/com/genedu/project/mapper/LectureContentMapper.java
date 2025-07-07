package com.genedu.project.mapper;

import com.genedu.project.dto.LectureContentRequestDTO;
import com.genedu.project.dto.LectureContentResponseDTO;
import com.genedu.project.model.LectureContent;
import com.genedu.project.model.Project;
import com.genedu.project.service.ProjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class LectureContentMapper {
    SlideContentMapper slideContentMapper;
    ProjectService projectService;

    public LectureContent toEntity(
            LectureContentRequestDTO lectureContentRequestDTO
    ) {
        UUID projectId = UUID.fromString(lectureContentRequestDTO.projectId());
        Project project  = projectService.getProjectEntityById(projectId);
        return LectureContent.builder()
                .project(project)
                .title(lectureContentRequestDTO.title())
                .build();
    }

    public LectureContentResponseDTO toDTO(
            LectureContent lectureContent
    ) {
        return new LectureContentResponseDTO(
                lectureContent.getId().toString(),
                lectureContent.getProject().getId().toString(),
                lectureContent.getTitle(),
                slideContentMapper.toDTOs(lectureContent.getSlideContents())
        );
    }

}
