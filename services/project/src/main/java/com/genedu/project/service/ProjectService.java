package com.genedu.project.service;

import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.commonlibrary.webclient.dto.LessonPlanFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.LessonPlanFileUploadDTO;
import com.genedu.project.dto.ProjectRequestDTO;
import com.genedu.project.dto.ProjectResponseDTO;
import com.genedu.project.model.Project;
import com.genedu.project.model.enumeration.ProjectStatus;
import com.genedu.project.utils.Constants;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public interface ProjectService {
    public List<ProjectResponseDTO> getAllProjects();

    public Project getProjectEntityById(UUID id);

    public List<ProjectResponseDTO> getProjectsByUserId(UUID userId);

    public ProjectResponseDTO getProjectById(UUID id);

    public ProjectResponseDTO createProject(ProjectRequestDTO projectDTO);

    public ProjectResponseDTO updateProject(UUID id, ProjectRequestDTO projectDetails);

    public void deleteProject(UUID id);

    public ProjectResponseDTO updateLessonPlanFile(LessonPlanFileUploadDTO lessonPlanFileUploadDTO);
    public List<ProjectResponseDTO> getCurrentUserProjects();

    public LessonPlanFileDownloadDTO getLessonPlanTemplate();
}
