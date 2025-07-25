package com.genedu.media.service;

import com.genedu.commonlibrary.kafka.dto.NotificationEvent;
import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.commonlibrary.webclient.dto.LectureVideoDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.LectureVideoUploadDTO;
import com.genedu.media.kafka.KafkaProducer;
import com.genedu.media.service.webclient.ProjectWebClientService;
import com.genedu.media.utils.SlideUtil;
import com.genedu.media.utils.VideoGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class VideoService {
    private final SlideFileServiceImpl slideFileService;
    private final NarrationAudioServiceImpl narrationAudioService;
    private final LectureVideoServiceImpl lectureVideoService;
    private final ProjectWebClientService projectWebClientService;
    private final KafkaProducer kafkaProducer;

    public void generateLectureVideo(
            String projectId, String lectureContentId, String finalizeLectureId,
            Long slideFileId, Map<Integer, Long> slideNarrationMap, String jwtToken) {
        // BEST PRACTICE: Create a single temporary directory for the entire job.
        Path workingDir = null;
        try {
            workingDir = Files.createTempDirectory("video-gen-job-");
            log.info("Created working directory for video generation: {}", workingDir);

            // 1. Download and process the presentation file.
            // This requires a new helper method in SlideFileServiceImpl to download to a specific path.
            Path pptxPath = slideFileService.downloadPptxToDirectory(slideFileId, workingDir);
            List<Path> slideImages = SlideUtil.convertToImages(pptxPath, workingDir);
            log.info("Converted presentation to {} slide images.", slideImages.size());

            if (slideImages.size() != slideNarrationMap.size()) {
                log.error("Mismatch between number of slides ({}) and narrations ({}). Aborting.", slideImages.size(), slideNarrationMap.size());
                throw new IllegalArgumentException("The number of slides and narrations must match.");
            }

            // 2. Download all narration audio files for each slide.
            List<Path> narrationAudioPaths = new ArrayList<>();
            for (int i = 0; i < slideImages.size(); i++) {
                Long narrationFileId = slideNarrationMap.get(i);
                if (narrationFileId == null) {
                    throw new IllegalArgumentException("Missing narration audio file ID for slide index: " + i);
                }

                if (narrationFileId == 1) {
                    // Using Silent audio file for slide without narration
                    log.debug("Using silent audio for slide {} as no narration is provided.", i);
                    Path silentAudioPath = narrationAudioService.getDefaultSilentAudio(workingDir);
                    narrationAudioPaths.add(silentAudioPath);
                    continue;
                }

                Path audioPath = narrationAudioService.downloadAudioToDirectory(narrationFileId, workingDir);
                narrationAudioPaths.add(audioPath);
                log.debug("Downloaded narration for slide {} to {}", i, audioPath);
            }

            // 3. Generate the final video.
            Path outputVideoPath = workingDir.resolve("final-video.mp4");
            VideoGenerator.generate(slideImages, narrationAudioPaths, outputVideoPath);
            log.info("Video generation successful. Final video created at: {}", outputVideoPath);

            LectureVideoDownloadDTO lectureVideoDownloadDTO = lectureVideoService.systematicSaveMediaFile(
                    new LectureVideoUploadDTO(
                            projectId,
                            lectureContentId,
                            finalizeLectureId,
                            Files.readAllBytes(outputVideoPath.toFile().toPath())
                    )
            );
            // 4. Update the finalized lecture with the new video file.
            projectWebClientService.updateFinalizedLectureVideo(
                    UUID.fromString(finalizeLectureId),
                    lectureVideoDownloadDTO.getId(),
                    jwtToken
            );

            log.info("Lecture video saved successfully for project ID: {}, lecture content ID: {}, finalize lecture ID: {}",
                    projectId, lectureContentId, finalizeLectureId);

        } catch (IOException e) {
            log.error("Video generation failed due to an I/O error.", e);
            throw new RuntimeException("Video generation failed", e);
        } finally {
            // 5. GUARANTEE cleanup of the entire working directory and all its contents.
            if (workingDir != null) {
                try {
                    // Use a robust utility like Spring's FileSystemUtils to recursively delete.
                    FileSystemUtils.deleteRecursively(workingDir);
                    log.info("Successfully cleaned up working directory: {}", workingDir);
                } catch (IOException e) {
                    log.error("CRITICAL: Failed to delete temporary working directory: {}. Manual cleanup required.", workingDir, e);
                }
            }
        }
    }
}
