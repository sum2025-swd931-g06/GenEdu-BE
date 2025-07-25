package com.genedu.project.mapper;

import com.genedu.project.dto.SlideContentRequestDTO;
import com.genedu.project.dto.SlideContentResponseDTO;
import com.genedu.project.model.SlideContent;
import com.genedu.project.service.impl.SlideContentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SlideContentMapper {
    private final SlideContentServiceImpl slideContentService;
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
                        slideContent.getNarrationScript(),
                        slideContentService.getSlideContentNarrationFileUrl(slideContent.getNarrationFileId())
                ))
                .toList();
    }
}
