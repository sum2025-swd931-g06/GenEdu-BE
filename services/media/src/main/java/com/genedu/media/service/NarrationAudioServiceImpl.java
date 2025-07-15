package com.genedu.media.service;

import com.genedu.commonlibrary.enumeration.FileType;
import com.genedu.commonlibrary.enumeration.SlideFileFormat;
import com.genedu.commonlibrary.enumeration.SlideNarrationAudioFormat;
import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.commonlibrary.webclient.dto.SlideFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.SlideFileUploadDTO;
import com.genedu.commonlibrary.webclient.dto.SlideNarrationAudioDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.SlideNarrationAudioUploadDTO;
import com.genedu.media.buckets.BucketName;
import com.genedu.media.model.MediaFile;
import com.genedu.media.repository.MediaFileRepository;
import com.genedu.media.repository.S3StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NarrationAudioServiceImpl implements MediaFileService<SlideNarrationAudioUploadDTO, SlideNarrationAudioDownloadDTO>{

    @Value("${aws.s3.endpoint}")
    private String S3Host;

    private final S3StorageService s3StorageService;
    private final MediaFileRepository mediaFileRepository;
    private final BucketName bucketName;
    private static final String PROJECT_FOLDER = "projects";

    @Override
    public void checkValidFormat(SlideNarrationAudioUploadDTO mediaFile) {
        if (mediaFile == null) {
            throw new BadRequestException("Media file cannot be null");
        }
        if (mediaFile.getAudioFile() == null || mediaFile.getAudioFile().length == 0) {
            throw new BadRequestException("Audio file cannot be null or empty");
        }
    }

    @Override
    public SlideNarrationAudioDownloadDTO saveMediaFile(SlideNarrationAudioUploadDTO mediaFile) throws IOException {
        UUID userId = Optional.ofNullable(AuthenticationUtils.getUserId())
                .orElseThrow(() -> new BadRequestException("User ID could not be determined from security context."));
        return saveFileInternal(mediaFile, FileType.AUDIO, userId);
    }

    @Override
    public SlideNarrationAudioDownloadDTO systematicSaveMediaFile(SlideNarrationAudioUploadDTO mediaFile) throws IOException {
        return null;
    }

    private SlideNarrationAudioDownloadDTO saveFileInternal(SlideNarrationAudioUploadDTO uploadDTO, FileType fileType, UUID uploadedBy) throws IOException {
        checkValidFormat(uploadDTO);

        byte[] file = uploadDTO.getAudioFile();

        String projectId = uploadDTO.getProjectId().toString();
        String fileName = uploadDTO.getSlideId() + "_narration" + ".mp3"; // Use a consistent naming convention for the file
        //define content type for mp3
        String contentType = "audio/mpeg"; // Default content type for MP3 files
        String filePath = String.join("/", PROJECT_FOLDER, projectId, "narrations", uploadDTO.getLectureContentId().toString(), uploadDTO.getSlideId().toString());
        String fullS3Key = String.join("/", filePath, fileName);

        // Find an existing file at the same path and mark it as deleted.
        mediaFileRepository.findFirstByFileUrlAndDeletedIsFalseOrderByUploadedOnDesc(fullS3Key)
                .ifPresent(existingFile -> {
                    // 1. Delete the old file from the S3 bucket
                    s3StorageService.delete(existingFile.getFileUrl());

                    // 2. Mark the old database record as deleted
                    existingFile.setDeleted(true);
                    mediaFileRepository.save(existingFile);
                });

        String fileUrl = s3StorageService.upload(filePath, fileName, contentType, file);

        // Create and save the new file's metadata
        MediaFile newMediaFile = new MediaFile();
        newMediaFile.setFileName(fileName);
        newMediaFile.setFileType(fileType);
        newMediaFile.setFileUrl(fileUrl);
        newMediaFile.setUploadedBy(uploadedBy);
        newMediaFile.setUploadedOn(LocalDateTime.now());
        newMediaFile.setDeleted(false); // Explicitly set as not deleted
        MediaFile savedMediaFile = mediaFileRepository.save(newMediaFile);

        return mapToDto(savedMediaFile);
    }

    private SlideNarrationAudioDownloadDTO mapToDto(MediaFile mediaFile) {
        String fileUrl = String.join("/",S3Host, bucketName.getGeneduBucket(), mediaFile.getFileUrl());
        return SlideNarrationAudioDownloadDTO.builder()
                .id(mediaFile.getId())
                .fileName(mediaFile.getFileName())
                .fileType(mediaFile.getFileType())
                .fileUrl(fileUrl)
                .uploadedBy(mediaFile.getUploadedBy())
                .uploadedOn(mediaFile.getUploadedOn()) // Assuming readFileContent returns content
                .build();
    }

    @Override
    public SlideNarrationAudioDownloadDTO getMediaFileByFileNameAndFileType(String fileName, FileType fileType) {
        return null;
    }

    @Override
    public String getMediaFileUrlById(Long id) {
        MediaFile mediaFile = mediaFileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Media file not found with id: " + id));
        return String.format("%s/%s/%s",S3Host, bucketName.getGeneduBucket(), mediaFile.getFileUrl());
    }

    @Override
    public void deleteMediaFile(Long id) {

    }

    @Override
    public SlideNarrationAudioDownloadDTO updateMediaFile(Long id, SlideNarrationAudioUploadDTO mediaFile) {
        return null;
    }

    @Override
    public List<SlideNarrationAudioDownloadDTO> getAllMediaFilesByOwnerId(UUID ownerId) {
        return List.of();
    }

    @Override
    public List<SlideNarrationAudioDownloadDTO> getMediaFilesByType(FileType type) {
        return List.of();
    }

    @Override
    public List<SlideNarrationAudioDownloadDTO> getMediaFilesByOwnerIdAndType(UUID ownerId, FileType type) {
        return List.of();
    }

    @Override
    public SlideNarrationAudioDownloadDTO readFileContent(Long id) {
        return null;
    }

    @Override
    public SlideNarrationAudioDownloadDTO getMediaFileByProjectId(String projectId) {
        return null;
    }
}
