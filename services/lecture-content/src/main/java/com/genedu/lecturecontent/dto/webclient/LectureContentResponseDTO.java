package com.genedu.lecturecontent.dto.webclient;

import java.util.List;

public record LectureContentResponseDTO(
        String id,
        String projectId,
        String title,
        List<SlideContentRequestDTO> slideContents
) {
}
