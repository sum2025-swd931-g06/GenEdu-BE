package com.genedu.project.mapper;

import com.genedu.project.dto.LectureContentRequestDTO;
import com.genedu.project.dto.LectureContentResponseDTO;
import com.genedu.project.model.LectureContent;
import com.genedu.project.model.Project;
import com.genedu.project.service.impl.ProjectServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class LectureContentMapper {
    SlideContentMapper slideContentMapper;
    ProjectServiceImpl projectServiceImpl;

    public LectureContent toEntity(
            LectureContentRequestDTO lectureContentRequestDTO
    ) {
        UUID projectId = UUID.fromString(lectureContentRequestDTO.projectId());
        Project project  = projectServiceImpl.getProjectEntityById(projectId);
        return LectureContent.builder()
                .project(project)
                .title(lectureContentRequestDTO.title())
                .build();
    }

    public LectureContentResponseDTO toDTO(
            LectureContent lectureContent
    ) {
        return new LectureContentResponseDTO(
                lectureContent.getId(),
                lectureContent.getProject().getId(),
                lectureContent.getTitle(),
                lectureContent.getStatus().toString(),
                slideContentMapper.toDTOs(lectureContent.getSlideContents())
        );
    }

    public List<LectureContentResponseDTO> toDTOs(
            List<LectureContent> lectureContents
    ) {
        return lectureContents.stream()
                .map(this::toDTO)
                .toList();
    }
}
