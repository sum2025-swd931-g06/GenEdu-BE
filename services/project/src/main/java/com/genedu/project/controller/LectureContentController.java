package com.genedu.project.controller;

import com.genedu.commonlibrary.webclient.dto.SlideFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.SlideFileUploadDTO;
import com.genedu.project.dto.FinalizedLectureCreateRequestDTO;
import com.genedu.project.dto.FinalizedLectureResponseDTO;
import com.genedu.project.dto.LectureContentRequestDTO;
import com.genedu.project.dto.LectureContentResponseDTO;
import com.genedu.project.model.FinalizedLecture;
import com.genedu.project.model.LectureContent;
import com.genedu.project.service.FinalizedLectureService;
import com.genedu.project.service.LectureContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Lecture Content Management", description = "APIs for creating and managing lecture content within a project")
public class LectureContentController {
    private final LectureContentService lectureContentService;
    private final FinalizedLectureService finalizedLectureService;

    @Operation(summary = "Get lecture contents by Project ID", description = "Retrieves the list of lecture contents associated with a specific project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved lecture content"),
            @ApiResponse(responseCode = "404", description = "Project or lecture content not found")
    })
    @GetMapping("/{projectId}/lecture-contents")
    public ResponseEntity<List<LectureContentResponseDTO>> getLectureContentByProjectId(
            @PathVariable("projectId") UUID projectId
    ) {
        List<LectureContentResponseDTO> lectureContentResponseDTOS = lectureContentService.getLectureContentByProjectId(projectId);
        return ResponseEntity.ok(lectureContentResponseDTOS);
    }

    @Operation(summary = "Create new lecture content", description = "Creates a new lecture content outline. The project ID must be provided in the request body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lecture content created successfully",
                    headers = @Header(name = "Location", description = "URL to the newly created resource")),
            @ApiResponse(responseCode = "400", description = "Invalid request data provided"),
            @ApiResponse(responseCode = "404", description = "Associated project not found")
    })
    @PostMapping("/lecture-contents")
    public ResponseEntity<LectureContentResponseDTO> createLectureContent(
            @RequestBody LectureContentRequestDTO lectureContentRequestDTO
    ) {
        LectureContentResponseDTO createdLecture = lectureContentService.createLectureContent(lectureContentRequestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{lectureContentId}")
                .buildAndExpand(createdLecture.id())
                .toUri();

        return ResponseEntity.created(location).body(createdLecture);
    }

    @Operation(summary = "Update lecture content", description = "Updates the details of an existing lecture content by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lecture content updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data provided"),
            @ApiResponse(responseCode = "404", description = "Lecture content with the given ID not found")
    })
    @PutMapping("/lecture-content/{lectureContentId}")
    public ResponseEntity<LectureContentResponseDTO> updateLectureContent(
            @PathVariable("lectureContentId") UUID lectureContentId,
            @RequestBody LectureContentRequestDTO lectureContentRequestDTO
    ) {
        LectureContentResponseDTO lectureContentResponseDTO = lectureContentService.updateLectureContent(lectureContentId, lectureContentRequestDTO);
        return ResponseEntity.ok(lectureContentResponseDTO);
    }

    @Operation(summary = "Trigger narration generation", description = "Starts an asynchronous process to generate narration for all slides within a lecture content.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Narration generation process successfully triggered."),
            @ApiResponse(responseCode = "404", description = "Lecture content with the given ID not found.")
    })
    @PostMapping("/lecture-content/{lectureContentId}/narration-generation-async")
    public ResponseEntity<Void> generateNarrationForLectureContentAsync(
            @PathVariable("lectureContentId") UUID lectureContentId
    ) {
        lectureContentService.generateNarrationForLectureContentAsyn(lectureContentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Trigger video generation", description = "Starts an asynchronous process to generate a lecture video for the specified lecture content.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Video generation process successfully triggered."),
            @ApiResponse(responseCode = "404", description = "Lecture content with the given ID not found.")
    })
    @PostMapping("/lecture-content/{finalizedLectureId}/video-generation-async")
    @Deprecated
    public ResponseEntity<Void> generateLectureVideoForLectureContentAsync(
            @PathVariable("finalizedLectureId") UUID finalizedLectureId
    ) {
        lectureContentService.generateLectureVideoForLectureContentAsyn(finalizedLectureId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Generate narration for lecture content", description = "Generates narration for all slides within a lecture content and returns the updated lecture content.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Narration successfully generated for the lecture content."),
            @ApiResponse(responseCode = "404", description = "Lecture content with the given ID not found.")
    })
    @PostMapping("/lecture-content/{lectureContentId}/narration-generation")
    @Deprecated
    public ResponseEntity<LectureContentResponseDTO> generateNarrationForLectureContent(
            @PathVariable("lectureContentId") UUID lectureContentId
    ) {
        LectureContentResponseDTO lectureContentResponseDTO = lectureContentService.generateNarrationForLectureContent(lectureContentId);
        return ResponseEntity.ok(lectureContentResponseDTO);
    }

    @Operation(summary = "Update narration audio for a slide - INTERNAL Service Call Only", description = "Associates a generated narration audio file with a specific slide content record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Narration audio successfully associated with the slide."),
            @ApiResponse(responseCode = "400", description = "Invalid request data provided (e.g., missing audioFileId)."),
            @ApiResponse(responseCode = "404", description = "Slide content with the given ID not found.")
    })
    @PutMapping("/lecture-content/slide-content/{slideContentId}/narration-audio")
    @Deprecated // This endpoint is deprecated and should not be used by external clients.
    public ResponseEntity<LectureContentResponseDTO> updateNarrationAudioForLectureContent(
            @PathVariable("slideContentId") UUID slideContentId,
            @RequestParam("audioFileId") Long audioFileId
    ) {
        lectureContentService.updateNarrationAudioForLectureContent(slideContentId, audioFileId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Upload the main presentation file for a lecture",
            description = "Uploads the primary presentation file (PPTX) for a specific lecture content. This action will create the associated finalized lecture record."
    )
    @PostMapping(
            value = "/lecture-content/presentations",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<FinalizedLectureResponseDTO> uploadLectureContentSlides(
            @ModelAttribute SlideFileUploadDTO slideFileUploadDTO
    ) {
        // Upload the slide file and get the download DTO
        SlideFileDownloadDTO slideFileDownloadDTO = lectureContentService.uploadSlideFile(slideFileUploadDTO);
        // Check if the upload was successful
        if (slideFileDownloadDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        List<FinalizedLecture> existFinalizedLectures = finalizedLectureService.getFinalizedLectureEntitiesByLectureContentId(UUID.fromString(slideFileUploadDTO.getLectureContentId()));
        FinalizedLectureResponseDTO finalizedLectureResponseDTO;

        if (!existFinalizedLectures.isEmpty()) {
            // If a finalized lecture already exists, update it with the new slide file
            FinalizedLecture finalizedLecture = existFinalizedLectures.get(0);
            finalizedLectureResponseDTO = finalizedLectureService.updateFinalizedLectureMedia(finalizedLecture.getId(),
                    new FinalizedLectureCreateRequestDTO(
                            finalizedLecture.getId(),
                            null,
                            slideFileDownloadDTO.getId(),
                            null,
                            null,
                            finalizedLecture.getPublishedStatus()
                    )
            );
        } else {
            // Create a new finalized lecture if none exists
            LectureContent lectureContent = lectureContentService.getLectureContentEntityById(UUID.fromString(slideFileUploadDTO.getLectureContentId()));
            FinalizedLectureCreateRequestDTO createRequest = new FinalizedLectureCreateRequestDTO(
                    lectureContent.getId(),
                    null,
                    slideFileDownloadDTO.getId(),
                    null,
                    null,
                    null
            );
            finalizedLectureResponseDTO = finalizedLectureService.createFinalizedLecture(createRequest);
        }
        return ResponseEntity.ok(finalizedLectureResponseDTO);
    }
}
