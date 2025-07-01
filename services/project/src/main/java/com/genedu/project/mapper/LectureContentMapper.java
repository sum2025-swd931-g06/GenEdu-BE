package com.genedu.project.mapper;

import com.genedu.project.dto.LectureContentRequestDTO;
import com.genedu.project.dto.LectureContentResponseDTO;
import com.genedu.project.model.LectureContent;
import com.genedu.project.model.Project;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class LectureContentMapper {
    SlideContentMapper slideContentMapper;

    public LectureContent toEntity(
            LectureContentRequestDTO lectureContentRequestDTO
    ) {
        Project project = new Project();
        project.setId(UUID.fromString(lectureContentRequestDTO.projectId()));

        return LectureContent.builder()
                .project(project)
                .title(lectureContentRequestDTO.title())
                .slideContents(slideContentMapper.toEntities(lectureContentRequestDTO.slideContents()))
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
