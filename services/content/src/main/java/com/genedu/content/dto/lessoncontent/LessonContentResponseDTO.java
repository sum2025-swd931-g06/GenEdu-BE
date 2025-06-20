package com.genedu.content.dto.lessoncontent;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.genedu.content.dto.lesson.LessonResponseDTO;
import com.genedu.content.dto.lessoncontentmediafile.LessonContentMediaFileResponseDTO;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record LessonContentResponseDTO(
        Long id,
        String title,
        Integer orderNumber,
        String content,
        ZonedDateTime createdOn,
        UUID createdBy,
        ZonedDateTime lastModifiedOn,
        UUID lastModifiedBy,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<LessonContentMediaFileResponseDTO> mediaFiles
) {
}
