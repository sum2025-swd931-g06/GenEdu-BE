package com.genedu.media.service;

import com.genedu.commonlibrary.enumeration.FileType;
import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.commonlibrary.webclient.dto.LectureVideoDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.LectureVideoUploadDTO;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class LectureVideoServiceImpl implements MediaFileService<LectureVideoUploadDTO, LectureVideoDownloadDTO>{
    @Value("${aws.s3.endpoint}")
    private String S3Host;

    private final S3StorageService s3StorageService;
    private final MediaFileRepository mediaFileRepository;
    private final BucketName bucketName;
    private static final String PROJECT_FOLDER = "projects";
    private final UUID SYSTEM_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Override
    public void checkValidFormat(LectureVideoUploadDTO mediaFile) {
        if (mediaFile == null) {
            throw new BadRequestException("Media file cannot be null");
        }
        if (mediaFile.getVideoFile() == null || mediaFile.getVideoFile().length == 0) {
            throw new BadRequestException("Video file cannot be null or empty");
        }
    }

    @Override
    public LectureVideoDownloadDTO saveMediaFile(LectureVideoUploadDTO mediaFile) throws IOException {
        UUID userId = Optional.ofNullable(AuthenticationUtils.getUserId())
                .orElseThrow(() -> new BadRequestException("User ID could not be determined from security context."));
        return saveFileInternal(mediaFile, FileType.VIDEO, userId);
    }
    
    public LectureVideoDownloadDTO saveMediaFile(LectureVideoUploadDTO uploadDTO, UUID userId) throws IOException {
        return saveFileInternal(uploadDTO, FileType.VIDEO, userId);
    }

    private LectureVideoDownloadDTO saveFileInternal(LectureVideoUploadDTO uploadDTO, FileType fileType, UUID uploadedBy) throws IOException {
        checkValidFormat(uploadDTO);

        byte[] file = uploadDTO.getVideoFile();

        String projectId = uploadDTO.getProjectId().toString();
        String fileName = uploadDTO.getFinalizeLectureId() + "_lecture_video" + ".mp4"; // Use a consistent naming convention for the file
        //define content type for mp3
        String contentType = "video/mp4"; // Default content type for MP3 files

        String filePath = String.join("/", PROJECT_FOLDER, projectId, "videos", uploadDTO.getLectureContentId().toString(), uploadDTO.getFinalizeLectureId().toString());
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

    private LectureVideoDownloadDTO mapToDto(MediaFile mediaFile) {
        String fileUrl = String.join("/",S3Host, bucketName.getGeneduBucket(), mediaFile.getFileUrl());
        return LectureVideoDownloadDTO.builder()
                .id(mediaFile.getId())
                .fileName(mediaFile.getFileName())
                .fileType(mediaFile.getFileType())
                .fileUrl(fileUrl)
                .uploadedBy(mediaFile.getUploadedBy())
                .uploadedOn(mediaFile.getUploadedOn()) // Assuming readFileContent returns content
                .build();
    }

    @Override
    public LectureVideoDownloadDTO systematicSaveMediaFile(LectureVideoUploadDTO mediaFile) throws IOException {
        return saveFileInternal(mediaFile, FileType.LESSON_PLAN_TEMPLATE, SYSTEM_USER_ID);
    }

    @Override
    public LectureVideoDownloadDTO getMediaFileByFileNameAndFileType(String fileName, FileType fileType) {
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
    public LectureVideoDownloadDTO updateMediaFile(Long id, LectureVideoUploadDTO mediaFile) {
        return null;
    }

    @Override
    public List<LectureVideoDownloadDTO> getAllMediaFilesByOwnerId(UUID ownerId) {
        return List.of();
    }

    @Override
    public List<LectureVideoDownloadDTO> getMediaFilesByType(FileType type) {
        return List.of();
    }

    @Override
    public List<LectureVideoDownloadDTO> getMediaFilesByOwnerIdAndType(UUID ownerId, FileType type) {
        return List.of();
    }

    @Override
    public LectureVideoDownloadDTO readFileContent(Long id) {
        return null;
    }

    @Override
    public LectureVideoDownloadDTO getMediaFileByProjectId(String projectId) {
        return null;
    }
}
