package com.genedu.project.controller;

import com.genedu.project.dto.FinalizedLectureResponseDTO;
import com.genedu.project.dto.LectureContentResponseDTO;
import com.genedu.project.service.impl.FinalizedLectureServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class FinalizedLectureController {

    private final FinalizedLectureServiceImpl finalizedLectureServiceImpl;

    @Operation(
            summary = "Get all finalized lectures for a specific project",
            description = "Retrieves a list of finalized lectures associated with a given lectureContent ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of finalized lectures"),
            @ApiResponse(responseCode = "404", description = "Project with the given ID not found")
    })
    @GetMapping("/{lectureContentId}/finalized-lectures")
    public ResponseEntity<List<FinalizedLectureResponseDTO>> getFinalizedLecture(
            @PathVariable("lectureContentId") UUID lectureContentId
    ) {
        List<FinalizedLectureResponseDTO> finalizedLectures = finalizedLectureServiceImpl.getFinalizedLecturesByLectureContentId(lectureContentId);
        return ResponseEntity.ok(finalizedLectures);
    }

    @Operation(
            summary = "Get a finalized lecture by its ID",
            description = "Retrieves the details of a finalized lecture using its unique ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the finalized lecture"),
            @ApiResponse(responseCode = "404", description = "Finalized lecture with the given ID not found")
    })
    @GetMapping("/finalized-lectures/{finalizedLectureId}")
    public ResponseEntity<FinalizedLectureResponseDTO> getFinalizedLectureById(
            @PathVariable("finalizedLectureId") UUID finalizedLectureId
    ) {
        FinalizedLectureResponseDTO finalizedLecture = finalizedLectureServiceImpl.getFinalizedLectureById(finalizedLectureId);
        return ResponseEntity.ok(finalizedLecture);
    }

    @Operation(
            summary = "Update a finalized lecture video",
            description = "Updates the narration audio for a finalized lecture video."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the finalized lecture video"),
            @ApiResponse(responseCode = "404", description = "Finalized lecture or lecture video not found")
    })
    @PutMapping("/finalized-lectures/{finalizedLectureId}/lecture-videos")
    @Deprecated // This endpoint is deprecated and should not be used by external clients.
    public ResponseEntity<LectureContentResponseDTO> updateLectureVideo(
            @PathVariable("finalizedLectureId") UUID finalizedLectureId,
            @RequestParam("lectureVideoId") Long lectureVideoId
    ) {
        finalizedLectureServiceImpl.updateNarrationAudioForLectureContent(finalizedLectureId, lectureVideoId);
        return ResponseEntity.ok().build();
    }
}
