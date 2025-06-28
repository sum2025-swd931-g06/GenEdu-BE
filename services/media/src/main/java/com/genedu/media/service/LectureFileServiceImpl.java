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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LectureFileServiceImpl implements MediaFileService<LessonPlanFileUploadDTO, LessonPlanFileDownloadDTO> {
    private final S3StorageService s3StorageService;
    private final MediaFileRepository mediaFileRepository;
    private final BucketName bucketName;
    private static final String LECTURE_FOLDER = "projects";
    private static final String S3Host = "http://localhost:4566";

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
                LessonPlanFileFormat.TXT.getExtension()
        );

        if (!validExtensions.contains(fileExtension.toLowerCase())) {
            throw new BadRequestException("Invalid file format. Supported formats are: " + validExtensions);
        }
    }


    @Override
    public LessonPlanFileDownloadDTO saveMediaFile(LessonPlanFileUploadDTO mediaFile) throws IOException {
        checkValidFormat(mediaFile);

        MultipartFile file = mediaFile.getMediaFile();
        String projectId = mediaFile.getProjectId();

        String fileName = file.getOriginalFilename();

        String filePath = LECTURE_FOLDER + "/" + projectId + "/lesson_plans";
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }
        String fileUrl = s3StorageService.upload(filePath, fileName, file);

        MediaFile newMediaFile = new MediaFile();
        newMediaFile.setFileName(fileName);
        newMediaFile.setFileType(FileType.LECTURE);
        newMediaFile.setFileUrl(fileUrl);
        newMediaFile.setUploadedBy(AuthenticationUtils.getUserId()); // Replace with actual user ID
        newMediaFile.setUploadedOn(java.time.LocalDateTime.now());
        MediaFile savedMediaFile = mediaFileRepository.save(newMediaFile);

        return LessonPlanFileDownloadDTO.builder()
                .id(savedMediaFile.getId())
                .fileName(savedMediaFile.getFileName())
                .fileType(savedMediaFile.getFileType())
                .fileUrl(savedMediaFile.getFileUrl())
                .uploadedBy(savedMediaFile.getUploadedBy())
                .uploadedOn(savedMediaFile.getUploadedOn())
                .build();
    }

    @Override
    public String getMediaFileUrlById(Long id) {
        MediaFile mediaFile = mediaFileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Media file not found with id: " + id));
        return String.format("%s/%s/%s",S3Host, bucketName.getLectureBucket(), mediaFile.getFileUrl());
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

