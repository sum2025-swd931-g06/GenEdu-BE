package com.genedu.content.controller;

import com.genedu.content.dto.chapter.ChapterRequestDTO;
import com.genedu.content.dto.flatResponse.FlatMaterialChapterDTO;
import com.genedu.content.dto.material.MaterialResponseDTO;
import com.genedu.content.service.ChapterService;
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
@Tag(name = "Chapter", description = "Manage chapters in the system")
public class ChapterController {
    private final ChapterService chapterService;

    @Operation(summary = "Get all chapters", description = "Returns a list of all chapters in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved chapter list"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/chapters")
    public ResponseEntity<List<FlatMaterialChapterDTO>> getAllChapters() {
        log.info("Fetching all chapters");
        return ResponseEntity.ok(chapterService.getAllChapters());
    }

    @GetMapping("/chapters/{id}")
    @Operation(summary = "Get chapter by ID", description = "Returns chapter information by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved chapter"),
            @ApiResponse(responseCode = "404", description = "Chapter not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<FlatMaterialChapterDTO> getChapterById(@PathVariable Long id) {
        log.info("Fetching chapter with ID: {}", id);
        FlatMaterialChapterDTO chapter = chapterService.getChapterById(id);
        return ResponseEntity.ok(chapter);
    }

    @GetMapping("/materials/{materialId}/chapters")
    @Operation(summary = "Get all chapters of a material", description = "Returns a list of all chapters of a material by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved chapter list"),
            @ApiResponse(responseCode = "404", description = "Material not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<MaterialResponseDTO> getChaptersByMaterialId(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID of the material to retrieve chapters for", required = true
            )
            @PathVariable Long materialId
    ) {
        log.info("Fetching chapters for material with ID: {}", materialId);

        return ResponseEntity.ok(
                chapterService.getMaterialWithChapters(materialId)
        );
    }

    @PostMapping("/materials/{materialId}/chapters")
    @Operation(summary = "Create a new chapter for a material", description = "Creates a new chapter for a material by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Chapter successfully created"),
            @ApiResponse(responseCode = "404", description = "Material not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<FlatMaterialChapterDTO> createChapter(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID of the material to create chapter for", required = true
            )
            @PathVariable Long materialId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Information of the chapter to create", required = true
            )
            @RequestBody ChapterRequestDTO chapterRequestDTO
    ) {
        log.info("Creating new chapter for material with ID: {}", materialId);
        FlatMaterialChapterDTO createdChapter = chapterService.createChapter(materialId, chapterRequestDTO);
        return ResponseEntity
                .created(URI.create("/api/v1/materials/" + materialId + "/chapters/" + createdChapter.chapterId()))
                .body(createdChapter);
    }

    @PutMapping("chapters/{id}")
    @Operation(summary = "Update a chapter", description = "Updates the information of a chapter by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chapter successfully updated"),
            @ApiResponse(responseCode = "404", description = "Chapter not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<FlatMaterialChapterDTO> updateChapter(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID of the chapter to update", required = true
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated information of the chapter", required = true
            )
            @RequestBody ChapterRequestDTO chapterRequestDTO
    ) {
        log.info("Updating chapter with ID: {}", id);
        FlatMaterialChapterDTO updatedChapter = chapterService.updateChapter(id, chapterRequestDTO);
        return ResponseEntity.ok(updatedChapter);
    }

    @DeleteMapping("/chapters/{id}")
    @Operation(summary = "Delete a chapter", description = "Deletes a chapter by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Chapter successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Chapter not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Void> deleteChapter(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID of the chapter to delete", required = true
            )
            @PathVariable Long id
    ) {
        log.info("Deleting chapter with ID: {}", id);
        chapterService.deleteChapter(id);
        return ResponseEntity.noContent().build();
    }

}
