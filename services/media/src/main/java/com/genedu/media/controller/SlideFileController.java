package com.genedu.media.controller;

import com.genedu.commonlibrary.webclient.dto.LessonPlanFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.LessonPlanFileUploadDTO;
import com.genedu.commonlibrary.webclient.dto.SlideFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.SlideFileUploadDTO;
import com.genedu.media.service.MediaFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/medias")
public class SlideFileController {
    private final MediaFileService<SlideFileUploadDTO, SlideFileDownloadDTO> mediaFileService;

    @PostMapping(
            path = "/projects/slides/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SlideFileDownloadDTO> uploadLectureFile(
            @ModelAttribute @Validated SlideFileUploadDTO file
    ) throws IOException {
        SlideFileDownloadDTO uploadedFile = mediaFileService.saveMediaFile(file);
        return ResponseEntity.ok(uploadedFile);
    }

    @GetMapping("/projects/slides/{fileId}/url")
    public ResponseEntity<String> getLessonPlanFileUrlByLessonPlanId(
            @PathVariable Long fileId
    ) {
        String fileUrl = mediaFileService.getMediaFileUrlById(fileId);
        return new ResponseEntity<>(fileUrl, HttpStatus.OK);
    }
}
