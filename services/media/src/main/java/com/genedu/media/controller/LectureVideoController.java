package com.genedu.media.controller;

import com.genedu.commonlibrary.webclient.dto.LectureVideoDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.LectureVideoUploadDTO;
import com.genedu.commonlibrary.webclient.dto.SlideNarrationAudioDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.SlideNarrationAudioUploadDTO;
import com.genedu.media.service.MediaFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/medias")
public class LectureVideoController {
    private final MediaFileService<LectureVideoUploadDTO, LectureVideoDownloadDTO> mediaFileService;

    @PostMapping(
            path = "/projects/lecture-video/upload"
    )
    public ResponseEntity<LectureVideoDownloadDTO> uploadLectureVideoFile(
            @RequestBody @Validated LectureVideoUploadDTO file
    ) throws IOException {
        LectureVideoDownloadDTO uploadedFile = mediaFileService.saveMediaFile(file);
        return ResponseEntity.ok(uploadedFile);
    }

    @GetMapping("/projects/lecture-video/{fileId}/url")
    public ResponseEntity<String> getNarrationFileUrlBySlideId(
            @PathVariable Long fileId
    ) {
        String fileUrl = mediaFileService.getMediaFileUrlById(fileId);
        return new ResponseEntity<>(fileUrl, HttpStatus.OK);
    }
}
