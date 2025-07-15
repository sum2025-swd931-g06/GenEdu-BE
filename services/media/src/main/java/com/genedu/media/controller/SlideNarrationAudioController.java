package com.genedu.media.controller;

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
public class SlideNarrationAudioController {

    private final MediaFileService<SlideNarrationAudioUploadDTO, SlideNarrationAudioDownloadDTO> mediaFileService;

    @PostMapping(
            path = "/projects/slides/narration-audios/upload"
    )
    public ResponseEntity<SlideNarrationAudioDownloadDTO> uploadLectureFile(
            @RequestBody @Validated SlideNarrationAudioUploadDTO file
    ) throws IOException {
        SlideNarrationAudioDownloadDTO uploadedFile = mediaFileService.saveMediaFile(file);
        return ResponseEntity.ok(uploadedFile);
    }

    @GetMapping("/projects/slides/narration-audios/{fileId}/url")
    public ResponseEntity<String> getNarrationFileUrlBySlideId(
            @PathVariable Long fileId
    ) {
        String fileUrl = mediaFileService.getMediaFileUrlById(fileId);
        return new ResponseEntity<>(fileUrl, HttpStatus.OK);
    }
}
