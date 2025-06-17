package com.genedu.content.controller;

import com.genedu.content.dto.schoolclass.SchoolClassRequestDTO;
import com.genedu.content.dto.schoolclass.SchoolClassResponseDTO;
import com.genedu.content.service.SchoolClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/contents/school-classes")
@Tag(name = "School Class", description = "Manage school classes in the system")
public class SchoolClassController {
    private final SchoolClassService schoolClassService;

    @Operation(summary = "Get all school classes", description = "Returns a list of all school classes in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of school classes"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<SchoolClassResponseDTO>> getAllSchoolClasses() {
        log.info("Fetching all school classes");
        List<SchoolClassResponseDTO> schoolClasses = schoolClassService.getAllSchoolClasses();
        return ResponseEntity.ok(schoolClasses);
    }

    @Operation(summary = "Get school class by ID", description = "Returns a school class by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the school class"),
            @ApiResponse(responseCode = "404", description = "School class not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<SchoolClassResponseDTO> getSchoolClassById(
            @Parameter(description = "ID of the school class", required = true)
            @PathVariable Integer id
    ) {
        log.info("Fetching school class with ID: {}", id);
        SchoolClassResponseDTO schoolClass = schoolClassService.getSchoolClassById(id);
        return ResponseEntity.ok(schoolClass);
    }

    @Operation(summary = "Create new school class", description = "Creates a new school class with name and description.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "School class created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<SchoolClassResponseDTO> createSchoolClass(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "School class data to create", required = true)
            @Valid @RequestBody SchoolClassRequestDTO schoolClassRequestDTO
    ) {
        log.info("Creating new school class: {}", schoolClassRequestDTO);
        SchoolClassResponseDTO created = schoolClassService.createSchoolClass(schoolClassRequestDTO);
        return ResponseEntity
                .created(URI.create("/api/v1/school-classes/" + created.id()))
                .body(created);
    }

    @Operation(summary = "Update school class", description = "Updates an existing school class by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "School class updated successfully"),
            @ApiResponse(responseCode = "404", description = "School class not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<SchoolClassResponseDTO> updateSchoolClass(
            @Parameter(description = "ID of the school class to update", required = true)
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated school class data", required = true)
            @Valid @RequestBody SchoolClassRequestDTO schoolClassRequestDTO
    ) {
        log.info("Updating school class with ID: {}, data: {}", id, schoolClassRequestDTO);
        SchoolClassResponseDTO updated = schoolClassService.updateSchoolClass(id, schoolClassRequestDTO);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete school class", description = "Deletes a school class by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "School class deleted successfully"),
            @ApiResponse(responseCode = "404", description = "School class not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchoolClass(
            @Parameter(description = "ID of the school class to delete", required = true)
            @PathVariable Integer id
    ) {
        log.info("Deleting school class with ID: {}", id);
        schoolClassService.deleteSchoolClass(id);
        return ResponseEntity.noContent().build();
    }
}
