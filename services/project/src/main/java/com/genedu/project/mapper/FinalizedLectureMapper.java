package com.genedu.project.mapper;

import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.project.dto.FinalizedLectureCreateRequestDTO;
import com.genedu.project.dto.FinalizedLectureResponseDTO;
import com.genedu.project.model.FinalizedLecture;
import com.genedu.project.model.LectureContent;
import com.genedu.project.model.enumeration.PublishedStatus;
import com.genedu.project.service.LectureContentService;
import com.genedu.project.service.impl.LectureContentServiceImpl;
import com.genedu.project.webclient.ProjectMediaWebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FinalizedLectureMapper {
    private final ProjectMediaWebClientService projectMediaWebClientService;

    public FinalizedLecture toEntity(
            FinalizedLectureCreateRequestDTO finalizedLectureCreateRequestDTO,
            LectureContent lectureContent
    ) {
        if (lectureContent == null) {
            throw new NotFoundException("Lecture content not found for ID: " + finalizedLectureCreateRequestDTO.lectureContentId());
        }
        return FinalizedLecture.builder()
                .lectureContent(lectureContent)
                .audioFileId(finalizedLectureCreateRequestDTO.audioFileId())
                .presentationFileId(finalizedLectureCreateRequestDTO.presentationFileId())
                .videoFileId(finalizedLectureCreateRequestDTO.videoFileId())
                .thumbnailFileId(finalizedLectureCreateRequestDTO.thumbnailFileId())
                .publishedStatus(
                        finalizedLectureCreateRequestDTO.publishedStatus() != null
                                ? finalizedLectureCreateRequestDTO.publishedStatus()
                                : PublishedStatus.PRIVATE
                )
                .build();
    }

    public FinalizedLectureResponseDTO toDTO(
            FinalizedLecture finalizedLecture
    ) {
        String audioFileUrl = projectMediaWebClientService.getAudioFileUrl(finalizedLecture.getAudioFileId());
        String presentationFileUrl = projectMediaWebClientService.getPresentationFileUrl(finalizedLecture.getPresentationFileId());
        String videoFileUrl = projectMediaWebClientService.getVideoFileUrl(finalizedLecture.getVideoFileId());
        String thumbnailFileUrl = projectMediaWebClientService.getThumbnailFileUrl(finalizedLecture.getThumbnailFileId());
        return new FinalizedLectureResponseDTO(
                finalizedLecture.getId(),
                finalizedLecture.getLectureContent().getId(),
                audioFileUrl,
                presentationFileUrl,
                videoFileUrl,
                thumbnailFileUrl,
                finalizedLecture.getPublishedStatus().name()
        );
    }
}
