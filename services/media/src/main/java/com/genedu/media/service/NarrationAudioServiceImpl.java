package com.genedu.media.service;

import com.genedu.commonlibrary.enumeration.FileType;
import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.FileStorageException;
import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.commonlibrary.webclient.dto.*;
import com.genedu.media.buckets.BucketName;
import com.genedu.media.model.MediaFile;
import com.genedu.media.repository.MediaFileRepository;
import com.genedu.media.repository.S3StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
    public SlideNarrationAudioDownloadDTO getMediaFileByFileNameAndFileType(String fileName, FileType fileType) {
        return mediaFileRepository.findFirstByFileNameAndFileTypeAndDeletedIsFalseOrderByUploadedOnDesc(fileName, fileType)
                .map(this::mapToDto)
                .orElseThrow(() -> new BadRequestException("Media file not found with name: " + fileName + " and type: " + fileType));
    }

    @Override
    public SlideNarrationAudioDownloadDTO saveMediaFile(SlideNarrationAudioUploadDTO mediaFile) throws IOException {
        UUID userId = Optional.ofNullable(AuthenticationUtils.getUserId())
                .orElseThrow(() -> new BadRequestException("User ID could not be determined from security context."));
        return saveFileInternal(mediaFile, FileType.AUDIO, userId);
    }

    @Override
    public SlideNarrationAudioDownloadDTO systematicSaveMediaFile(SlideNarrationAudioUploadDTO mediaFile) throws IOException {
        UUID userId = new UUID(0, 0); // Use a system user ID for systematic operations
        return saveFileInternal(mediaFile, FileType.AUDIO, userId);
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
    public String getMediaFileUrlById(Long id) {
        MediaFile mediaFile = mediaFileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Media file not found with id: " + id));
        return String.format("%s/%s/%s",S3Host, bucketName.getGeneduBucket(), mediaFile.getFileUrl());
    }

    public Path downloadAudioToDirectory(Long fileId, Path targetDirectory) {
        MediaFile mediaFile = mediaFileRepository.findByIdAndDeletedIsFalse(fileId)
                .orElseThrow(() -> new IllegalArgumentException("Media file not found with id: " + fileId));

        // Use the original file name for the downloaded file.
        Path destinationPath = targetDirectory.resolve(mediaFile.getFileName());

        try {
            s3StorageService.downloadToFile(mediaFile.getFileUrl(), destinationPath);
            log.debug("Downloaded file {} to {}", mediaFile.getFileUrl(), destinationPath);
            return destinationPath;
        } catch (IOException e) {
            throw new FileStorageException("Failed to download narration audio file with id: " + fileId, e);
        }
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


    public Path createSilentAudio(double durationInSeconds, Path workingDir) {
        try {
            // 1. Define a standard audio format (e.g., 44.1kHz, 16-bit, mono, signed PCM)
            float sampleRate = 44100;
            int sampleSizeInBits = 16;
            int channels = 1; // mono
            boolean signed = true;
            boolean bigEndian = false;
            AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);

            // 2. Calculate the number of bytes needed for the specified duration
            // Formula: (sampleRate * bitsPerSample / 8 * channels * duration)
            int bytesPerFrame = format.getFrameSize();
            long numFrames = (long) (durationInSeconds * format.getFrameRate());
            int totalBytes = (int) (numFrames * bytesPerFrame);

            // 3. Create a byte array of zeros, which represents silence in PCM audio
            byte[] audioData = new byte[totalBytes]; // Java initializes byte arrays to 0, which is silence

            Path silentAudioPath = workingDir.resolve("silent.wav");
            File outputFile = silentAudioPath.toFile();

            // 4. Use a try-with-resources block to create an AudioInputStream from the silent byte array
            try (AudioInputStream audioInputStream = new AudioInputStream(
                    new ByteArrayInputStream(audioData),
                    format,
                    numFrames
            )) {
                // 5. Write the audio stream to the output file in WAV format
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, outputFile);
            }

            log.debug("Created {}s silent audio file at {}", durationInSeconds, silentAudioPath);
            return silentAudioPath;
        } catch (IOException e) {
            throw new FileStorageException("Failed to create silent audio file", e);
        }
    }

    public Path getDefaultSilentAudio(Path workingDir) {
        // Now calls the generator to create a 2-second silent audio file as requested.
        return createSilentAudio(2.0, workingDir);
    }
}
