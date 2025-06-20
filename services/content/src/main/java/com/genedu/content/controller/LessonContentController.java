package com.genedu.content.controller;

import com.genedu.content.dto.flatResponse.FlatLessonLessonContentDTO;
import com.genedu.content.dto.lesson.LessonResponseDTO;
import com.genedu.content.dto.lessoncontent.LessonContentRequestDTO;
import com.genedu.content.service.LessonContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "LessonContent", description = "Manage lesson content in the system")
public class LessonContentController {
    private final LessonContentService lessonContentService;

    @Operation(summary = "Get all lesson content", description = "Returns a list of all lesson content in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved lesson content list"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/lesson-contents")
    public ResponseEntity<List<FlatLessonLessonContentDTO>> getAllLessonContents() {
        log.info("Fetching all lesson contents");
        var lessonContents = lessonContentService.getAllLessonContents();
        return ResponseEntity.ok(lessonContents);
    }

    @Operation(summary = "Get lesson content by ID", description = "Returns detailed information of a lesson content by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved lesson content"),
            @ApiResponse(responseCode = "404", description = "Lesson content not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/lesson-contents/{lessonContentId}")
    public ResponseEntity<FlatLessonLessonContentDTO> getLessonContentById(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID of the lesson content to retrieve", required = true
            )
            Long lessonContentId
    ) {
        log.info("Fetching lesson content with ID: {}", lessonContentId);
        var lessonContent = lessonContentService.getLessonContentById(lessonContentId);
        return ResponseEntity.ok(lessonContent);
    }

    @GetMapping("/lessons/{lessonId}/lesson-contents")
    @Operation(summary = "Get all lesson contents for a lesson", description = "Returns a list of all lesson contents that belong to a specific lesson by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved lesson contents for the lesson"),
            @ApiResponse(responseCode = "404", description = "Lesson not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LessonResponseDTO> getAllLessonContentsByLessonId(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID of the lesson to retrieve contents for", required = true
            )
            Long lessonId
    ) {
        log.info("Fetching all lesson contents for lesson with ID: {}", lessonId);
        var lessonContents = lessonContentService.getLessonContentsByLessonId(lessonId);
        return ResponseEntity.ok(lessonContents);
    }

    @PostMapping("/lessons/{lessonId}/lesson-contents")
    @Operation(summary = "Create lesson content for a lesson", description = "Creates a new lesson content for a specific lesson by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created lesson content"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<FlatLessonLessonContentDTO> createLessonContent(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID of the lesson to create content for", required = true
            )
            @PathVariable Long lessonId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the lesson content to create", required = true
            )
            @RequestBody LessonContentRequestDTO lessonContentRequest
    ) {
        log.info("Creating lesson content for lesson with ID: {}", lessonId);
        var createdLessonContent = lessonContentService.createLessonContent(lessonId, lessonContentRequest);
        return ResponseEntity
                .created(URI.create("/api/v1/contents/lesson-contents/" + createdLessonContent.lessonContentId()))
                .body(createdLessonContent);
    }

    @PutMapping("/lesson-contents/{lessonContentId}")
    @Operation(summary = "Update lesson content", description = "Updates an existing lesson content by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated lesson content"),
            @ApiResponse(responseCode = "404", description = "Lesson content not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<FlatLessonLessonContentDTO> updateLessonContent(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID of the lesson content to update", required = true
            )
            @PathVariable Long lessonContentId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated details of the lesson content", required = true
            )
            @RequestBody LessonContentRequestDTO lessonContentRequest
    ) {
        log.info("Updating lesson content with ID: {}", lessonContentId);
        var updatedLessonContent = lessonContentService.updateLessonContent(lessonContentId, lessonContentRequest);
        return ResponseEntity.ok(updatedLessonContent);
    }

    @DeleteMapping("/lesson-contents/{lessonContentId}")
    @Operation(summary = "Delete lesson content", description = "Deletes a lesson content by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted lesson content"),
            @ApiResponse(responseCode = "404", description = "Lesson content not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteLessonContent(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID of the lesson content to delete", required = true
            )
            @PathVariable Long lessonContentId
    ) {
        log.info("Deleting lesson content with ID: {}", lessonContentId);
        lessonContentService.deleteLessonContent(lessonContentId);
        return ResponseEntity.noContent().build();
    }
}
