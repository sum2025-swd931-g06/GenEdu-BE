package com.genedu.project.controller;

import com.genedu.commonlibrary.webclient.dto.SlideFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.SlideFileUploadDTO;
import com.genedu.project.dto.FinalizedLectureCreateRequestDTO;
import com.genedu.project.dto.LectureContentRequestDTO;
import com.genedu.project.dto.LectureContentResponseDTO;
import com.genedu.project.model.FinalizedLecture;
import com.genedu.project.model.enumeration.PublishedStatus;
import com.genedu.project.service.FinalizedLectureService;
import com.genedu.project.service.LectureContentService;
import com.genedu.project.service.impl.LectureContentServiceImpl;
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
    private final LectureContentService lectureContentServiceImpl;
    private final FinalizedLectureService finalizedLectureService;

    @Operation(summary = "Get lecture contents by Project ID", description = "Retrieves the list of lecture contents associated with a specific project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved lecture content"),
            @ApiResponse(responseCode = "404", description = "Project or lecture content not found")
    })
    @GetMapping("/{projectId}/lecture-content")
    public ResponseEntity<List<LectureContentResponseDTO>> getLectureContentByProjectId(
            @PathVariable("projectId") UUID projectId
    ) {
        List<LectureContentResponseDTO> lectureContentResponseDTOS = lectureContentServiceImpl.getLectureContentByProjectId(projectId);
        return ResponseEntity.ok(lectureContentResponseDTOS);
    }

    @Operation(summary = "Create new lecture content", description = "Creates a new lecture content outline. The project ID must be provided in the request body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lecture content created successfully",
                    headers = @Header(name = "Location", description = "URL to the newly created resource")),
            @ApiResponse(responseCode = "400", description = "Invalid request data provided"),
            @ApiResponse(responseCode = "404", description = "Associated project not found")
    })
    @PostMapping("/lecture-content")
    public ResponseEntity<LectureContentResponseDTO> createLectureContent(
            @RequestBody LectureContentRequestDTO lectureContentRequestDTO
    ) {
        LectureContentResponseDTO createdLecture = lectureContentServiceImpl.createLectureContent(lectureContentRequestDTO);

        finalizedLectureService.createFinalizedLecture(
                new FinalizedLectureCreateRequestDTO(
                        createdLecture.id(),
                        null,
                        null,
                        null,
                        null,
                        PublishedStatus.PRIVATE
                )
        );

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
        LectureContentResponseDTO lectureContentResponseDTO = lectureContentServiceImpl.updateLectureContent(lectureContentId, lectureContentRequestDTO);
        return ResponseEntity.ok(lectureContentResponseDTO);
    }

    @PostMapping("/lecture-content/narration-generation")
    public ResponseEntity<Void> generateNarrationForLectureContent(
            @RequestBody List<SlideFileDownloadDTO> slideFileDownloadDTOs
    ) {
        // TODO: Implement the logic to generate narration for lecture content
        return ResponseEntity.ok().build();
    }

    @PostMapping(
            value = "/lecture-content/slides",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> uploadLectureContentSlides(
            @ModelAttribute SlideFileUploadDTO slideFileUploadDTO
    ) {
        // Upload the slide file and get the download DTO
        SlideFileDownloadDTO slideFileDownloadDTO = lectureContentServiceImpl.uploadSlideFile(slideFileUploadDTO);
        // Check if the upload was successful
        if (slideFileDownloadDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        List<FinalizedLecture> existFinalizedLectures = finalizedLectureService.getFinalizedLectureEntitiesByLectureContentId(UUID.fromString(slideFileUploadDTO.getLectureContentId()));
        if (!existFinalizedLectures.isEmpty()) {
            FinalizedLecture finalizedLecture = existFinalizedLectures.get(0);
            finalizedLectureService.updateFinalizedLectureMedia(finalizedLecture.getId(),
                    new FinalizedLectureCreateRequestDTO(
                            finalizedLecture.getId(),
                            null,
                            slideFileDownloadDTO.getId(),
                            null,
                            null,
                            finalizedLecture.getPublishedStatus()
                    )
            );
        }
        return ResponseEntity.ok().build();
    }
}
