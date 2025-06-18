package com.genedu.project.service;

import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.project.dto.ProjectCreationDTO;
import com.genedu.project.model.Project;
import com.genedu.project.model.enumeration.ProjectStatus;
import com.genedu.project.repository.ProjectRepository;
import com.genedu.project.utils.Constants;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.ListTokenSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findByIsDeletedIsFalse();
    }

    public List<Project> getProjectsByUserId(UUID userId) {
        List<Project> projects = projectRepository.findByUserIdAndIsDeletedIsFalse(userId);
        if (projects.isEmpty()) {
            throw new NotFoundException(Constants.ErrorCode.PROJECT_WITH_USERID_NOT_FOUND, userId);
        }
        return projects;
    }

    public Project getProjectById(UUID id) {
        return projectRepository.findByIdAndIsDeletedIsFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
    }

    @Transactional
    public Project createProject(ProjectCreationDTO projectDTO) {
        Project project = new Project();
        project.setUserId(AuthenticationUtils.getUserId());
        project.setLessonId(projectDTO.getLessonId());
        project.setTitle(projectDTO.getTitle());
        project.setLessonPlanFileId(projectDTO.getLessonPlanFileId());
        project.setCustomInstructions(projectDTO.getCustomInstructions());
        project.setSlideNum(projectDTO.getSlideNum() != null ? projectDTO.getSlideNum() : 10);
        project.setStatus(ProjectStatus.DRAFT);
        project.setDeleted(false);

        return projectRepository.save(project);
    }

    @Transactional
    public Project updateProject(UUID id, Project projectDetails) {
        Project project = getProjectById(id);

        // Update the project fields
        project.setTitle(projectDetails.getTitle());
        project.setLessonPlanFileId(projectDetails.getLessonPlanFileId());
        project.setCustomInstructions(projectDetails.getCustomInstructions());
        project.setStatus(projectDetails.getStatus());
        project.setSlideNum(projectDetails.getSlideNum());

        return projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(UUID id) {
        Project project = getProjectById(id);
        project.setDeleted(true);
        projectRepository.save(project);
    }
}
