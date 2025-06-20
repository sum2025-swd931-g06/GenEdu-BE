package com.genedu.media.service;


import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.media.buckets.BucketName;
import com.genedu.media.model.MediaFile;
import com.genedu.commonlibrary.enumeration.FileType;
import com.genedu.media.repository.MediaFileRepository;
import com.genedu.media.repository.S3StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.genedu.media.dto.LessonPlanFileDownloadDTO;
import com.genedu.media.dto.LessonPlanFileUploadDTO;
import java.io.IOException;
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
    public LessonPlanFileDownloadDTO saveMediaFile(LessonPlanFileUploadDTO mediaFile) throws IOException {
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
}

