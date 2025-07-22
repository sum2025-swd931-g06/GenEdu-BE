package com.genedu.media.service;

import com.genedu.commonlibrary.enumeration.FileType;
import com.genedu.commonlibrary.enumeration.SlideFileFormat;
import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.FileStorageException;
import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.commonlibrary.webclient.dto.LessonPlanFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.SlideFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.SlideFileUploadDTO;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlideFileServiceImpl implements MediaFileService<SlideFileUploadDTO, SlideFileDownloadDTO> {
    @Value("${aws.s3.endpoint}")
    private String S3Host;

    private final S3StorageService s3StorageService;
    private final MediaFileRepository mediaFileRepository;
    private final BucketName bucketName;
    private static final String PROJECT_FOLDER = "projects";

    @Override
    public void checkValidFormat(SlideFileUploadDTO mediaFile) {
        String fileName = mediaFile.getMediaFile().getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new BadRequestException("File name cannot be null or empty");
        }
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

        List<String> validExtensions = List.of(
                SlideFileFormat.PPTX.getExtension()
        );

        if (!validExtensions.contains(fileExtension.toLowerCase())) {
            throw new BadRequestException("Invalid file format. Supported formats are: " + validExtensions);
        }
    }

    @Override
    public SlideFileDownloadDTO saveMediaFile(SlideFileUploadDTO mediaFile) throws IOException {
        UUID userId = Optional.ofNullable(AuthenticationUtils.getUserId())
                .orElseThrow(() -> new BadRequestException("User ID could not be determined from security context."));
        return saveFileInternal(mediaFile, FileType.SLIDE, userId);
    }

    @Override
    public SlideFileDownloadDTO systematicSaveMediaFile(SlideFileUploadDTO mediaFile) throws IOException {
        return null;
    }

    private SlideFileDownloadDTO saveFileInternal(SlideFileUploadDTO uploadDTO, FileType fileType, UUID uploadedBy) throws IOException {
        checkValidFormat(uploadDTO);

        MultipartFile file = uploadDTO.getMediaFile();
        String projectId = uploadDTO.getProjectId().toString();

        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.isEmpty()) {
            throw new BadRequestException("File name cannot be null or empty");
        }

        String filePath = String.join("/", PROJECT_FOLDER, projectId, "slides", uploadDTO.getLectureContentId().toString());
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
        // Upload the new file to S3
        String fileUrl = s3StorageService.upload(filePath, fileName, file);

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

    private SlideFileDownloadDTO mapToDto(MediaFile mediaFile) {
        String fileUrl = String.join("/",S3Host, bucketName.getGeneduBucket(), mediaFile.getFileUrl());
        return SlideFileDownloadDTO.builder()
                .id(mediaFile.getId())
                .fileName(mediaFile.getFileName())
                .fileType(mediaFile.getFileType())
                .fileUrl(fileUrl)
                .uploadedBy(mediaFile.getUploadedBy())
                .uploadedOn(mediaFile.getUploadedOn()) // Assuming readFileContent returns content
                .build();
    }

    @Override
    public SlideFileDownloadDTO getMediaFileByFileNameAndFileType(String fileName, FileType fileType) {
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
        MediaFile mediaFile = mediaFileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Media file not found with id: " + id));

        // Delete the file from S3
        s3StorageService.delete(mediaFile.getFileUrl());

        // Mark the file as deleted in the database
        mediaFile.setDeleted(true);
        mediaFileRepository.save(mediaFile);
    }

    @Override
    public SlideFileDownloadDTO updateMediaFile(Long id, SlideFileUploadDTO mediaFile) {
        return null;
    }

    @Override
    public List<SlideFileDownloadDTO> getAllMediaFilesByOwnerId(UUID ownerId) {
        return List.of();
    }

    @Override
    public List<SlideFileDownloadDTO> getMediaFilesByType(FileType type) {
        return List.of();
    }

    @Override
    public List<SlideFileDownloadDTO> getMediaFilesByOwnerIdAndType(UUID ownerId, FileType type) {
        return List.of();
    }

    @Override
    public SlideFileDownloadDTO readFileContent(Long id) {
        MediaFile mediaFile = mediaFileRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Media file not found with id: " + id));
        String fileUrl = String.format("%s/%s/%s", S3Host, bucketName.getGeneduBucket(), mediaFile.getFileUrl());
        return SlideFileDownloadDTO.builder()
                .id(mediaFile.getId())
                .fileName(mediaFile.getFileName())
                .fileType(mediaFile.getFileType())
                .fileUrl(fileUrl)
                .uploadedBy(mediaFile.getUploadedBy())
                .uploadedOn(mediaFile.getUploadedOn())
                .build();
    }

    @Override
    public SlideFileDownloadDTO getMediaFileByProjectId(String projectId) {
        return null;
    }

    public Path downloadPptxToDirectory(Long fileId, Path targetDirectory) {
        MediaFile mediaFile = mediaFileRepository.findByIdAndDeletedIsFalse(fileId)
                .orElseThrow(() -> new IllegalArgumentException("No slide PPTX found for fileId: " + fileId));

        // Use the original file name for the downloaded file.
        Path destinationPath = targetDirectory.resolve(mediaFile.getFileName());

        try {
            s3StorageService.downloadToFile(mediaFile.getFileUrl(), destinationPath);
            log.debug("Downloaded file {} to {}", mediaFile.getFileUrl(), destinationPath);
            return destinationPath;
        } catch (IOException e) {
            throw new FileStorageException("Failed to download slide presentation file with id: " + fileId, e);
        }
    }
}
