package com.genedu.project.dto;

import java.util.UUID;

public record FinalizedLectureResponseDTO(
        UUID id,
        UUID lectureContentId,
        String audioFileUrl,
        String presentationFileUrl,
        String videoFileUrl,
        String thumbnailFileUrl,
        String publishedStatus
) {
}
