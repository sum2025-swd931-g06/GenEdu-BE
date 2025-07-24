package com.genedu.project.controller;

import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.commonlibrary.webclient.dto.LessonPlanFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.LessonPlanFileUploadDTO;
import com.genedu.project.dto.ProjectResponseDTO;
import com.genedu.project.dto.ProjectRequestDTO;
import com.genedu.project.service.impl.ProjectServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Project Management", description = "APIs for creating, managing, and retrieving projects")
public class ProjectController {
    private final ProjectServiceImpl projectServiceImpl;

    @Operation(summary = "Get a project by its ID", description = "Retrieves the details of a single project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the project"),
            @ApiResponse(responseCode = "404", description = "Project with the given ID not found")
    })
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable UUID projectId) {
        ProjectResponseDTO project = projectServiceImpl.getProjectById(projectId);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @Operation(summary = "Get all projects", description = "Retrieves a list of all projects in the system. Use with caution in production.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of projects")
    })
    @GetMapping()
    public ResponseEntity<Page<ProjectResponseDTO>> getAllProjects(
            @PageableDefault(size = 10, sort = "lastModifiedOn", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        UUID userId = AuthenticationUtils.getUserId();
        Page<ProjectResponseDTO> projects = projectServiceImpl.getProjectsByUserId(userId, pageable);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @Operation(summary = "Create a new project", description = "Creates a new project with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid project data provided")
    })
    @PostMapping()
    public ResponseEntity<ProjectResponseDTO> createProject(
            @Valid @RequestBody ProjectRequestDTO projectDTO
    ) {
        ProjectResponseDTO createdProject = projectServiceImpl.createProject(projectDTO);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @Operation(summary = "Get lesson plan template", description = "Downloads the default lesson plan template file.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the template"),
            @ApiResponse(responseCode = "404", description = "Template file not found")
    })
    @GetMapping("/lesson-plan-template")
    public ResponseEntity<LessonPlanFileDownloadDTO> getLessonPlanTemplate() {
        LessonPlanFileDownloadDTO lessonPlanTemplate = projectServiceImpl.getLessonPlanTemplate();
        return new ResponseEntity<>(lessonPlanTemplate, HttpStatus.OK);
    }

    @Operation(summary = "Upload a lesson plan", description = "Uploads or updates the lesson plan file for a specific project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lesson plan uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file or project ID provided"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @PostMapping(
            value = "/lesson-plans",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ProjectResponseDTO> uploadLessonPlanFile(
            @ModelAttribute LessonPlanFileUploadDTO lessonPlanFileUploadDTO
    ) {
        ProjectResponseDTO updatedProject = projectServiceImpl.updateLessonPlanFile(lessonPlanFileUploadDTO);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @Operation(summary = "Update a project", description = "Updates the details of an existing project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid project data provided"),
            @ApiResponse(responseCode = "404", description = "Project with the given ID not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> updateProject(
            @PathVariable UUID id,
            @Valid @RequestBody ProjectRequestDTO projectDetails) {
        ProjectResponseDTO updatedProject = projectServiceImpl.updateProject(id, projectDetails);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @Operation(summary = "Delete a project", description = "Deletes a project by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Project deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Project with the given ID not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        projectServiceImpl.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
