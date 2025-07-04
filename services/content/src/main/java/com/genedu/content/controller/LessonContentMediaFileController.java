package com.genedu.content.controller;

import com.genedu.content.dto.flatResponse.FlatLessonContentLessonContentMediaFile;
import com.genedu.content.dto.lessoncontent.LessonContentResponseDTO;
import com.genedu.content.dto.lessoncontentmediafile.LessonContentMediaFileRequestDTO;
import com.genedu.content.model.LessonContentMediaFile;
import com.genedu.content.service.LessonContentMediaFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/contents")
@Tag(name = "LessonContentMediaFile", description = "Manage media files associated with lesson content")
public class LessonContentMediaFileController {
    private final LessonContentMediaFileService lessonContentMediaFileService;

    @GetMapping("/lesson-content-media-files")
    public ResponseEntity<List<FlatLessonContentLessonContentMediaFile>> getAllLessonContentMediaFiles() {
        log.info("Fetching all lesson content media files");
        List<FlatLessonContentLessonContentMediaFile> mediaFiles = lessonContentMediaFileService.getAllLessonContentMediaFiles();
        return ResponseEntity.ok(mediaFiles);
    }

    @GetMapping("/lesson-content-media-files/{id}")
    public ResponseEntity<FlatLessonContentLessonContentMediaFile> getLessonContentMediaFileById(@PathVariable Long id) {
        log.info("Fetching lesson content media file with ID: {}", id);
        FlatLessonContentLessonContentMediaFile mediaFile = lessonContentMediaFileService.getLessonContentMediaFileById(id);
        return ResponseEntity.ok(mediaFile);
    }

    @GetMapping("/lesson-contents/{lessonContentId}/esson-content-media-files")
    public ResponseEntity<LessonContentResponseDTO> getLessonContentMediaFilesByLessonContentId(@PathVariable Long lessonContentId) {
        log.info("Fetching media files for lesson content with ID: {}", lessonContentId);
        var mediaFiles = lessonContentMediaFileService.getLessonContentMediaFilesByLessonContentId(lessonContentId);
        return ResponseEntity.ok(mediaFiles);
    }

    @PostMapping("/lesson-contents/{lessonContentId}/lesson-content-media-files")
    public ResponseEntity<FlatLessonContentLessonContentMediaFile> createLessonContentMediaFile(
            @PathVariable Long lessonContentId,
            @RequestBody LessonContentMediaFileRequestDTO lessonContentMediaFileRequestDTO) {
        log.info("Creating new lesson content media file for lesson content ID: {}", lessonContentId);
        FlatLessonContentLessonContentMediaFile createdMediaFile = lessonContentMediaFileService.createLessonContentMediaFile(lessonContentId, lessonContentMediaFileRequestDTO);
        return ResponseEntity
                .created(URI.create("/api/v1/contents/lesson-content-media-files/" + createdMediaFile.lessonContentMediaFileId()))
                .body(createdMediaFile);
    }

    @PutMapping("/lesson-content-media-files/{id}")
    public ResponseEntity<FlatLessonContentLessonContentMediaFile> updateLessonContentMediaFile(
            @PathVariable Long id,
            @RequestBody LessonContentMediaFileRequestDTO lessonContentMediaFileRequestDTO) {
        log.info("Updating lesson content media file with ID: {}", id);
        FlatLessonContentLessonContentMediaFile updatedMediaFile = lessonContentMediaFileService.updateLessonContentMediaFile(id, lessonContentMediaFileRequestDTO);
        return ResponseEntity.ok(updatedMediaFile);
    }

    @PutMapping("/lesson-content-media-files/batch")
    @Operation(summary = "Update multiple lesson content media file", description = "Updates a list of lesson content media files.")
    public ResponseEntity<List<FlatLessonContentLessonContentMediaFile>> updateChapters(
            @RequestBody List<LessonContentMediaFileRequestDTO> requestDTOS) {
        var updated = lessonContentMediaFileService.updateLessonContentMediaFiles(requestDTOS);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/lesson-content-media-files/{id}")
    public ResponseEntity<Void> deleteLessonContentMediaFile(@PathVariable Long id) {
        log.info("Deleting lesson content media file with ID: {}", id);
        lessonContentMediaFileService.deleteLessonContentMediaFile(id);
        return ResponseEntity.noContent().build();
    }
}
