package com.genedu.media.service;


import com.genedu.commonlibrary.enumeration.LessonPlanFileFormat;
import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.commonlibrary.webclient.dto.LessonPlanFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.LessonPlanFileUploadDTO;
import com.genedu.media.buckets.BucketName;
import com.genedu.media.model.MediaFile;
import com.genedu.commonlibrary.enumeration.FileType;
import com.genedu.media.repository.MediaFileRepository;
import com.genedu.media.repository.S3StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LectureFileServiceImpl implements MediaFileService<LessonPlanFileUploadDTO, LessonPlanFileDownloadDTO> {
    private final S3StorageService s3StorageService;
    private final MediaFileRepository mediaFileRepository;
    private final BucketName bucketName;
    private static final String PROJECT_FOLDER = "projects";

    @Value("${aws.s3.endpoint}")
    private String S3Host;

    private final UUID SYSTEM_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Override
    @Transactional
    public LessonPlanFileDownloadDTO saveMediaFile(LessonPlanFileUploadDTO mediaFile) throws IOException {
        UUID userId = Optional.ofNullable(AuthenticationUtils.getUserId())
                .orElseThrow(() -> new BadRequestException("User ID could not be determined from security context."));
        return saveFileInternal(mediaFile, FileType.LESSON_PLAN, userId);
    }

    @Override
    @Transactional
    public LessonPlanFileDownloadDTO systematicSaveMediaFile(LessonPlanFileUploadDTO mediaFile) throws IOException {
        return saveFileInternal(mediaFile, FileType.LESSON_PLAN_TEMPLATE, SYSTEM_USER_ID);
    }

    @Override
    public LessonPlanFileDownloadDTO getMediaFileByFileNameAndFileType(String fileName, FileType fileType) {
        return mediaFileRepository.findFirstByFileNameAndFileTypeAndDeletedIsFalseOrderByUploadedOnDesc(fileName, fileType)
                .map(this::mapToDto)
                .orElseThrow(() -> new BadRequestException("Media file not found with name: " + fileName + " and type: " + fileType));
    }

    private LessonPlanFileDownloadDTO saveFileInternal(LessonPlanFileUploadDTO uploadDTO, FileType fileType, UUID uploadedBy) throws IOException {
        checkValidFormat(uploadDTO);

        MultipartFile file = uploadDTO.getMediaFile();
        String projectId = uploadDTO.getProjectId();
        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.isEmpty()) {
            throw new BadRequestException("File name cannot be null or empty");
        }

        String filePath = String.join("/", PROJECT_FOLDER, projectId, "lesson_plans");
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

    private LessonPlanFileDownloadDTO mapToDto(MediaFile mediaFile) {
        String fileUrl = String.format("%s/%s/%s",S3Host, bucketName.getGeneduBucket(), mediaFile.getFileUrl());
        return LessonPlanFileDownloadDTO.builder()
                .id(mediaFile.getId())
                .fileName(mediaFile.getFileName())
                .fileType(mediaFile.getFileType())
                .fileUrl(fileUrl)
                .uploadedBy(mediaFile.getUploadedBy())
                .uploadedOn(mediaFile.getUploadedOn()) // Assuming readFileContent returns content
                .build();
    }

    @Override
    public void checkValidFormat(LessonPlanFileUploadDTO mediaFile) {
        String fileName = mediaFile.getMediaFile().getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new BadRequestException("File name cannot be null or empty");
        }
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

        List<String> validExtensions = List.of(
                LessonPlanFileFormat.DOCX.getExtension(),
                LessonPlanFileFormat.PDF.getExtension(),
                LessonPlanFileFormat.TXT.getExtension(),
                LessonPlanFileFormat.MD.getExtension()
        );

        if (!validExtensions.contains(fileExtension.toLowerCase())) {
            throw new BadRequestException("Invalid file format. Supported formats are: " + validExtensions);
        }
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
    public LessonPlanFileDownloadDTO updateMediaFile(Long id, LessonPlanFileUploadDTO mediaFile) {
        return null;
    }

    @Override
    public List<LessonPlanFileDownloadDTO> getAllMediaFilesByOwnerId(UUID ownerId) {
        return null;
    }

    @Override
    public List<LessonPlanFileDownloadDTO> getMediaFilesByType(FileType type) {
        return null;
    }

    @Override
    public List<LessonPlanFileDownloadDTO> getMediaFilesByOwnerIdAndType(UUID ownerId, FileType type) {
        return null;
    }

    @Override
    public LessonPlanFileDownloadDTO readFileContent(Long id) {
        MediaFile mediaFile = mediaFileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Media file not found with id: " + id));
        byte[] fileContent;
        LessonPlanFileDownloadDTO lessonPlanFileDownloadDTO = new LessonPlanFileDownloadDTO();
        String content;

        try {
            fileContent = s3StorageService.download(mediaFile.getFileUrl());
            if (fileContent.length == 0) {
                throw new IllegalArgumentException("File content is empty for id: " + id);
            }
            String fileType = mediaFile.getFileName().substring(mediaFile.getFileName().lastIndexOf(".") + 1);
            if (fileType.equalsIgnoreCase("docx") || fileType.equalsIgnoreCase("doc")) {
                content = readWordFileContent(fileContent);
            } else {
                content = new String(fileContent, StandardCharsets.UTF_8);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read file content", e);
        }

        lessonPlanFileDownloadDTO.setId(mediaFile.getId());
        lessonPlanFileDownloadDTO.setFileName(mediaFile.getFileName());
        lessonPlanFileDownloadDTO.setFileType(mediaFile.getFileType());
        lessonPlanFileDownloadDTO.setFileUrl(mediaFile.getFileUrl());
        lessonPlanFileDownloadDTO.setUploadedBy(mediaFile.getUploadedBy());
        lessonPlanFileDownloadDTO.setUploadedOn(mediaFile.getUploadedOn());
        lessonPlanFileDownloadDTO.setContent(content);

        return lessonPlanFileDownloadDTO;
    }

    @Override
    public LessonPlanFileDownloadDTO getMediaFileByProjectId(String projectId) {
        String fileUrlPrefix = String.format("%s/%s/%s/", PROJECT_FOLDER, projectId, "lesson_plans");
        Optional<MediaFile> mediaFile = mediaFileRepository.findFirstByFileUrlStartingWithAndFileTypeAndDeletedIsFalseOrderByUploadedOnDesc(fileUrlPrefix, FileType.LESSON_PLAN);
        if (mediaFile.isEmpty()) {
            throw new IllegalArgumentException("Media file not found for project ID: " + projectId);
        }
        LessonPlanFileDownloadDTO lessonPlanFileDownloadDTO = mapToDto(mediaFile.get());
        lessonPlanFileDownloadDTO.setContent(readFileContent(mediaFile.get().getId()).getContent());
        return lessonPlanFileDownloadDTO;
    }

    private String readWordFileContent(byte[] fileContent) throws IOException {
        try (
            InputStream inputStream = new ByteArrayInputStream(fileContent);
            XWPFDocument document = new XWPFDocument(inputStream)
        ) {
            StringBuilder content = new StringBuilder();
            document.getParagraphs().forEach(paragraph -> content.append(paragraph.getText()).append("\n"));
            return content.toString();
        }
    }
}

