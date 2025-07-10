package com.genedu.project.dto;

import com.genedu.project.model.enumeration.PublishedStatus;

import java.util.UUID;

public record FinalizedLectureCreateRequestDTO(
        UUID lectureContentId,
        Long audioFileId,
        Long presentationFileId,
        Long videoFileId,
        Long thumbnailFileId,
        PublishedStatus publishedStatus
) {
}
