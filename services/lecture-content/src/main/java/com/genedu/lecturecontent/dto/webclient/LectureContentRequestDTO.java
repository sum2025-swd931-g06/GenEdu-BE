package com.genedu.lecturecontent.dto.webclient;

import com.genedu.commonlibrary.exception.BadRequestException;
import java.util.List;

public record LectureContentRequestDTO(
    String projectId,
    String title,
    List<SlideContentRequestDTO> slideContents
) {
    public LectureContentRequestDTO {
        if (projectId == null || projectId.isBlank()) {
            throw new BadRequestException("Project ID cannot be null or blank");
        }
        if (title == null || title.isBlank()) {
            throw new BadRequestException("Title cannot be null or blank");
        }
        if (slideContents == null || slideContents.isEmpty()) {
            throw new BadRequestException("Slide contents cannot be null or empty");
        }
    }
}
