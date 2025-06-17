package com.genedu.content.controller;

import com.genedu.content.dto.flatResponse.FlatMaterialChapterDTO;
import com.genedu.content.dto.flatResponse.FlatSchoolClassSubjectDTO;
import com.genedu.content.dto.flatResponse.FlatSubjectMaterialDTO;
import com.genedu.content.dto.material.MaterialRequestDTO;
import com.genedu.content.dto.schoolclass.SchoolClassResponseDTO;
import com.genedu.content.dto.subject.SubjectRequestDTO;
import com.genedu.content.service.MaterialService;
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
@RequestMapping("/api/v1/contents")
@Tag(name = "Material", description = "Manage subjects in the system")
public class MaterialController {
    private final MaterialService materialService;

    @Operation(summary = "Get all subjects", description = "Returns a list of all subjects in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved material list"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/materials")
    public ResponseEntity<List<FlatSubjectMaterialDTO>> getAllMaterials() {
        log.info("Fetching all materials");
        List<FlatSubjectMaterialDTO> subjects = materialService.getAllMaterials();
        return ResponseEntity.ok(subjects);
    }

    @Operation(summary = "Get material by ID", description = "Returns detailed information of a material by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved material"),
            @ApiResponse(responseCode = "404", description = "Material not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/materials/{id}")
    public ResponseEntity<FlatSubjectMaterialDTO> getMaterialsById(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID of the material to retrieve", required = true
            )
            @PathVariable Long id
    ) {
        log.info("Fetching material with ID: {}", id);
        FlatSubjectMaterialDTO subject = materialService.getMaterialById(id);
        return ResponseEntity.ok(subject);
    }

    @GetMapping("/school-classes/{materialId}/subjects")
    @Operation(summary = "Get all materials for a subject", description = "Returns a list of all subjects that belong to a specific school class by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved material list"),
            @ApiResponse(responseCode = "404", description = "School class not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<SchoolClassResponseDTO> getMaterialBySchoolClassId(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID of the school class to retrieve subjects", required = true
            )
            @PathVariable Integer subjectId
    ) {
        log.info("Fetching subjects for school class with ID: {}", subjectId);
        SchoolClassResponseDTO subjects = materialService.getMaterialsBySubjectId(subjectId);
        return ResponseEntity.ok(subjects);
    }

    @Operation(summary = "Create a new material", description = "Creates a new material for the given school class ID with the provided information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Material successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("school-classes/{materialId}/subjects")
    public ResponseEntity<FlatSubjectMaterialDTO> createSubject(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID of the school class to create material", required = true
            )
            @PathVariable Integer subjectId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Information of the material to be created", required = true
            )
            @RequestBody MaterialRequestDTO materialRequestDTO
    ) {
        log.info("Creating new material: {}", materialRequestDTO);
        FlatSubjectMaterialDTO createdMaterial = materialService.createMaterial(subjectId, materialRequestDTO);
        return ResponseEntity
                .created(URI.create("/api/v1/materials/" + createdMaterial.materialId()))
                .body(createdMaterial);
    }

    @Operation(summary = "Update a material", description = "Updates the information of a material by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Material successfully updated"),
            @ApiResponse(responseCode = "404", description = "Material not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/materials/{id}")
    public ResponseEntity<FlatSubjectMaterialDTO> updateSubject(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID of the material to be updated", required = true
            )
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated information of the material", required = true
            )
            @RequestBody MaterialRequestDTO materialRequestDTO
    ) {
        log.info("Updating material with ID: {}", id);
        FlatSubjectMaterialDTO updatedMaterial = materialService.updateMaterial(id, materialRequestDTO);
        return ResponseEntity.ok(updatedMaterial);
    }
}
