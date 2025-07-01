package com.genedu.project.mapper;

import com.genedu.project.dto.SlideContentRequestDTO;
import com.genedu.project.model.LectureContent;
import com.genedu.project.model.SlideContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SlideContentMapper {
    SlideContent toEntity(
            SlideContentRequestDTO slideContentRequestDTO
    ) {
        LectureContent lectureContent = LectureContent.builder()
                .id(UUID.fromString(slideContentRequestDTO.lectureContentId()))
                .build();

        return SlideContent.builder()
                .lectureContent(lectureContent)
                .orderNumber(slideContentRequestDTO.orderNumber())
                .slideTitle(slideContentRequestDTO.title())
                .mainIdea(slideContentRequestDTO.mainIdea())
                .subpoints(slideContentRequestDTO.subpoints())
                .build();

    }

    List<SlideContent> toEntities(
            List<SlideContentRequestDTO> slideContentRequestDTOs
    ) {
        return slideContentRequestDTOs.stream()
                .map(this::toEntity)
                .toList();
    }

    List<SlideContentRequestDTO> toDTOs(
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
