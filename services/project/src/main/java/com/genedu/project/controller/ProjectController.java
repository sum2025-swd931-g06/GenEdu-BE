package com.genedu.project.controller;

import com.genedu.commonlibrary.webclient.dto.LessonPlanFileUploadDTO;
import com.genedu.project.dto.ProjectResponseDTO;

import com.genedu.project.dto.ProjectRequestDTO;
import com.genedu.project.model.Project;
import com.genedu.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable UUID id) {
        ProjectResponseDTO project = projectService.getProjectById(id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }
    
    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        List<ProjectResponseDTO> projects = projectService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByUserId(@PathVariable UUID userId) {
        List<ProjectResponseDTO> projects = projectService.getProjectsByUserId(userId);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/my-projects")
    public ResponseEntity<List<ProjectResponseDTO>> getCurrentUserProjects() {
        List<ProjectResponseDTO> projects = projectService.getCurrentUserProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ProjectResponseDTO> createProject(
            @Valid @RequestBody ProjectRequestDTO projectDTO
    ) {
        ProjectResponseDTO createdProject = projectService.createProject(projectDTO);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @PutMapping(
            value = "/{projectId}/lesson-plan",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ProjectResponseDTO> uploadLectureFile(
            @ModelAttribute LessonPlanFileUploadDTO lectureFileUploadDTO,
            @PathVariable UUID projectId
    ) {
        ProjectResponseDTO updatedProject = projectService.updateLessonPlanFile(lectureFileUploadDTO, projectId);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> updateProject(
            @PathVariable UUID id,
            @Valid @RequestBody ProjectRequestDTO projectDetails) {
        ProjectResponseDTO updatedProject = projectService.updateProject(id, projectDetails);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
