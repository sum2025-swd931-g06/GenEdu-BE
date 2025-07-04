package com.genedu.project.mapper;

import com.genedu.project.dto.SlideContentRequestDTO;
import com.genedu.project.model.LectureContent;
import com.genedu.project.model.SlideContent;
import com.genedu.project.service.LectureContentService;
import com.genedu.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SlideContentMapper {
    LectureContentService lectureContentService;
    public SlideContent toEntity(SlideContentRequestDTO slideContentRequestDTO) {
        if (slideContentRequestDTO.lectureContentId() == null || slideContentRequestDTO.lectureContentId().isBlank()) {
            return SlideContent.builder()
                    .orderNumber(slideContentRequestDTO.orderNumber())
                    .slideTitle(slideContentRequestDTO.title())
                    .mainIdea(slideContentRequestDTO.mainIdea())
                    .subpoints(slideContentRequestDTO.subpoints())
                    .build();
        } else {
            LectureContent lectureContent = lectureContentService.getLectureContentEntityById(
                    UUID.fromString(slideContentRequestDTO.lectureContentId())
            );

            return SlideContent.builder()
                    .lectureContent(lectureContent)
                    .orderNumber(slideContentRequestDTO.orderNumber())
                    .slideTitle(slideContentRequestDTO.title())
                    .mainIdea(slideContentRequestDTO.mainIdea())
                    .subpoints(slideContentRequestDTO.subpoints())
                    .build();
        }
    }

    public List<SlideContent> toEntities(
            List<SlideContentRequestDTO> slideContentRequestDTOs
    ) {
        return slideContentRequestDTOs.stream()
                .map(this::toEntity)
                .toList();
    }

    public List<SlideContentRequestDTO> toDTOs(
            List<SlideContent> slideContents
    ) {
        return slideContents.stream()
                .map(slideContent -> new SlideContentRequestDTO(
                        slideContent.getLectureContent().getId().toString(),
                        slideContent.getSlideTitle(),
                        slideContent.getMainIdea(),
                        slideContent.getOrderNumber(),
                        slideContent.getSubpoints(),
                        slideContent.getNarrationScript()
                ))
                .toList();
    }
}
