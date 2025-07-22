package com.genedu.media.controller;

import com.genedu.commonlibrary.webclient.dto.LessonPlanFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.LessonPlanFileUploadDTO;
import com.genedu.commonlibrary.webclient.dto.SlideFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.SlideFileUploadDTO;
import com.genedu.media.service.MediaFileService;
import com.genedu.media.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/medias")
public class SlideFileController {
    private final MediaFileService<SlideFileUploadDTO, SlideFileDownloadDTO> mediaFileService;
    private final VideoService videoService;

    @PostMapping(
            path = "/projects/slides/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SlideFileDownloadDTO> uploadSlideFile(
            @ModelAttribute @Validated SlideFileUploadDTO file
    ) throws IOException {
        SlideFileDownloadDTO uploadedFile = mediaFileService.saveMediaFile(file);
        return ResponseEntity.ok(uploadedFile);
    }

    @GetMapping("/projects/slides/{fileId}/url")
    public ResponseEntity<String> getSlideFileUrlByFileId(
            @PathVariable Long fileId
    ) {
        String fileUrl = mediaFileService.getMediaFileUrlById(fileId);
        return new ResponseEntity<>(fileUrl, HttpStatus.OK);
    }

    @PostMapping(
            path = "/projects/slides/video"
    )
    public ResponseEntity<String> testVideoGeneration(
            @RequestParam String projectId,
            @RequestParam String lectureContentId,
            @RequestParam String finalizeLectureId,
            @RequestParam Long slideFileId,
            @RequestBody Map<Integer, Long> narrationAudios
    ) {
        Path videoPath = Paths.get("/tmp/project/output-test.mp4");
        videoService.generateLectureVideo(projectId, lectureContentId, finalizeLectureId, slideFileId, narrationAudios, "test-jwt");
        return new ResponseEntity<>("Video generation started for project ID: " + "test", HttpStatus.OK);
    }
}
