package com.genedu.project.mapper;

import com.genedu.project.dto.SlideContentRequestDTO;
import com.genedu.project.dto.SlideContentResponseDTO;
import com.genedu.project.model.LectureContent;
import com.genedu.project.model.SlideContent;
import com.genedu.project.service.impl.LectureContentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SlideContentMapper {
    LectureContentServiceImpl lectureContentServiceImpl;
    public SlideContent toEntity(SlideContentRequestDTO slideContentRequestDTO) {
        if (slideContentRequestDTO.lectureContentId() == null || slideContentRequestDTO.lectureContentId().isBlank()) {
            return SlideContent.builder()
                    .orderNumber(slideContentRequestDTO.orderNumber())
                    .slideTitle(slideContentRequestDTO.title())
                    .slideType(slideContentRequestDTO.slideType())
                    .subpoints(slideContentRequestDTO.subpoints())
                    .narrationScript(slideContentRequestDTO.narrationScript())
                    .build();
        } else {
            LectureContent lectureContent = lectureContentServiceImpl.getLectureContentEntityById(
                    UUID.fromString(slideContentRequestDTO.lectureContentId())
            );

            return SlideContent.builder()
                    .lectureContent(lectureContent)
                    .orderNumber(slideContentRequestDTO.orderNumber())
                    .slideTitle(slideContentRequestDTO.title())
                    .slideType(slideContentRequestDTO.slideType())
                    .subpoints(slideContentRequestDTO.subpoints())
                    .narrationScript(slideContentRequestDTO.narrationScript())
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

    public List<SlideContentResponseDTO> toDTOs(
            List<SlideContent> slideContents
    ) {
        return slideContents.stream()
                .map(slideContent -> new SlideContentResponseDTO(
                        slideContent.getLectureContent().getId().toString(),
                        slideContent.getSlideTitle(),
                        slideContent.getSlideType(),
                        slideContent.getOrderNumber(),
                        slideContent.getSubpoints(),
                        slideContent.getNarrationScript()
                ))
                .toList();
    }
}
