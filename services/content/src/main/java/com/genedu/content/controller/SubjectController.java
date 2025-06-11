package com.genedu.content.controller;

import com.genedu.content.dto.flatResponse.FlatSchoolClassSubjectDTO;
import com.genedu.content.dto.schoolclass.SchoolClassResponseDTO;
import com.genedu.content.dto.subject.SubjectRequestDTO;
import com.genedu.content.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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
@RequestMapping("/api/v1")
@Tag(name = "Subject", description = "Manage subjects in the system")
public class SubjectController {

    private final SubjectService subjectService;

    @Operation(summary = "Get all subjects", description = "Returns a list of all subjects in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved subject list"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/subjects")
    public ResponseEntity<List<FlatSchoolClassSubjectDTO>> getAllSubjects() {
        log.info("Fetching all subjects");
        List<FlatSchoolClassSubjectDTO> subjects = subjectService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }

    @Operation(summary = "Get subject by ID", description = "Returns detailed information of a subject by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved subject"),
            @ApiResponse(responseCode = "404", description = "Subject not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/subjects/{id}")
    public ResponseEntity<FlatSchoolClassSubjectDTO> getSubjectById(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID of the subject to retrieve", required = true
            )
            @PathVariable Long id
    ) {
        log.info("Fetching subject with ID: {}", id);
        FlatSchoolClassSubjectDTO subject = subjectService.getSubjectById(id);
        return ResponseEntity.ok(subject);
    }

    @GetMapping("/school-classes/{schoolClassId}/subjects")
    @Operation(summary = "Get all subjects for a school class", description = "Returns a list of all subjects that belong to a specific school class by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved subject list"),
            @ApiResponse(responseCode = "404", description = "School class not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<SchoolClassResponseDTO> getSubjectsBySchoolClassId(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID of the school class to retrieve subjects", required = true
            )
            @PathVariable Integer schoolClassId
    ) {
        log.info("Fetching subjects for school class with ID: {}", schoolClassId);
        SchoolClassResponseDTO subjects = subjectService.getSubjectsBySchoolClassId(schoolClassId);
        return ResponseEntity.ok(subjects);
    }

    @Operation(summary = "Create a new subject", description = "Creates a new subject for the given school class ID with the provided information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subject successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("school-classes/{schoolClassId}/subjects")
    public ResponseEntity<FlatSchoolClassSubjectDTO> createSubject(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID of the school class to create subject", required = true
            )
            @PathVariable Integer schoolClassId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Information of the subject to be created", required = true
            )
            @RequestBody SubjectRequestDTO subjectRequestDTO
    ) {
        log.info("Creating new subject: {}", subjectRequestDTO);
        FlatSchoolClassSubjectDTO createdSubject = subjectService.createSubject(schoolClassId, subjectRequestDTO);
        return ResponseEntity
                .created(URI.create("/api/v1/subjects/" + createdSubject.subjectId()))
                .body(createdSubject);
    }

    @Operation(summary = "Update a subject", description = "Updates the information of a subject by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject successfully updated"),
            @ApiResponse(responseCode = "404", description = "Subject not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/subjects/{id}")
    public ResponseEntity<FlatSchoolClassSubjectDTO> updateSubject(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID of the subject to be updated", required = true
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated information of the subject", required = true
            )
            @RequestBody SubjectRequestDTO subjectRequestDTO
    ) {
        log.info("Updating subject with ID: {}", id);
        FlatSchoolClassSubjectDTO updatedSubject = subjectService.updateSubject(id, subjectRequestDTO);
        return ResponseEntity.ok(updatedSubject);
    }

    /*
    @Operation(summary = "Delete a subject", description = "Deletes a subject from the system by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Subject successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Subject not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID of the subject to be deleted", required = true
            )
            @PathVariable Long id
    ) {
        log.info("Deleting subject with ID: {}", id);
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }
    */
}
