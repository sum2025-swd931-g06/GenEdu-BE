package com.genedu.media.controller;

import com.genedu.commonlibrary.webclient.dto.LessonPlanFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.LessonPlanFileUploadDTO;
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
public class LessonPlanFileController {

    private final MediaFileService<LessonPlanFileUploadDTO, LessonPlanFileDownloadDTO> mediaFileService;

    @PostMapping(
            path = "/projects/lesson-plans/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<LessonPlanFileDownloadDTO> uploadLectureFile(
            @ModelAttribute @Validated LessonPlanFileUploadDTO file
    ) throws IOException {
        LessonPlanFileDownloadDTO uploadedFile = mediaFileService.saveMediaFile(file);
        return ResponseEntity.ok(uploadedFile);
    }

    @GetMapping("/projects/lesson-plans/{fileId}")
    public ResponseEntity<LessonPlanFileDownloadDTO> getLessonPlanFileById(
            @PathVariable Long fileId
    ) {
        LessonPlanFileDownloadDTO lessonPlanFile = mediaFileService.readFileContent(fileId);
        return new ResponseEntity<>(lessonPlanFile, HttpStatus.OK);
    }

    @GetMapping("/projects/lesson-plans/{fileId}/url")
    public ResponseEntity<String> getLessonPlanFileUrlByLessonPlanId(
            @PathVariable Long fileId
    ) {
        String fileUrl = mediaFileService.getMediaFileUrlById(fileId);
        return new ResponseEntity<>(fileUrl, HttpStatus.OK);
    }
}

