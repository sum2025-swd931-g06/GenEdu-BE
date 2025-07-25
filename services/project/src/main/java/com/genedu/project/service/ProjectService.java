package com.genedu.project.service;
import com.genedu.commonlibrary.webclient.dto.LessonPlanFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.LessonPlanFileUploadDTO;
import com.genedu.project.dto.ProjectRequestDTO;
import com.genedu.project.dto.ProjectResponseDTO;
import com.genedu.project.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;


public interface ProjectService {
    public Project getProjectEntityById(UUID id);

    public Page<ProjectResponseDTO> getProjectsByUserId(UUID userId, Pageable pageable);

    public ProjectResponseDTO getProjectById(UUID id);

    public ProjectResponseDTO createProject(ProjectRequestDTO projectDTO);

    public ProjectResponseDTO updateProject(UUID id, ProjectRequestDTO projectDetails);

    public void deleteProject(UUID id);

    public ProjectResponseDTO updateLessonPlanFile(LessonPlanFileUploadDTO lessonPlanFileUploadDTO);

    public LessonPlanFileDownloadDTO getLessonPlanTemplate();
}
