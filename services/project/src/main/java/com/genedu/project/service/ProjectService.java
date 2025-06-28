package com.genedu.project.service;

import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.commonlibrary.webclient.dto.LessonPlanFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.LessonPlanFileUploadDTO;
import com.genedu.project.dto.ProjectRequestDTO;
import com.genedu.project.dto.ProjectResponseDTO;
import com.genedu.project.mapper.ProjectMapper;
import com.genedu.project.model.Project;
import com.genedu.project.model.enumeration.ProjectStatus;
import com.genedu.project.repository.ProjectRepository;
import com.genedu.project.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final LectureMediaWebClientService lectureMediaWebClientService;
    private final ProjectMapper projectMapper;

    public List<ProjectResponseDTO> getAllProjects() {
        List<Project> projects = projectRepository.findByIsDeletedIsFalse();
        List<ProjectResponseDTO> projectResponseDTOs = projects.stream()
                .map(projectMapper::toDTO)
                .toList();
        if (projectResponseDTOs.isEmpty()) {
            throw new NotFoundException(Constants.ErrorCode.PROJECT_NOT_FOUND);
        }
        return projectResponseDTOs;
    }

    public Project getProjectEntityById(UUID id) {
        return projectRepository.findByIdAndIsDeletedIsFalse(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.PROJECT_NOT_FOUND, id));
    }

    public List<ProjectResponseDTO> getProjectsByUserId(UUID userId) {
        List<Project> projects = projectRepository.findByUserIdAndIsDeletedIsFalse(userId);
        if (projects.isEmpty()) {
            throw new NotFoundException(Constants.ErrorCode.PROJECT_WITH_USERID_NOT_FOUND, userId);
        }

        return projects.stream()
                .map(projectMapper::toDTO)
                .toList();
    }

    public ProjectResponseDTO getProjectById(UUID id) {
        Optional<Project> project = projectRepository.findByIdAndIsDeletedIsFalse(id);
        if (project.isEmpty()) {
            throw new NotFoundException(Constants.ErrorCode.PROJECT_NOT_FOUND, id);
        }
        return projectMapper.toDTO(project.get());
    }

    @Transactional
    public ProjectResponseDTO createProject(ProjectRequestDTO projectDTO) {
        Project project = new Project();

        project.setUserId(AuthenticationUtils.getUserId());
        project.setLessonId(projectDTO.lessonId());
        project.setTitle(projectDTO.title());
        project.setCustomInstructions(projectDTO.customInstructions());
        project.setSlideNum(projectDTO.slideNumber() != null ? projectDTO.slideNumber() : 10);
        project.setStatus(ProjectStatus.DRAFT);
        project.setDeleted(false);

        Project savedProject = projectRepository.save(project);

        return projectMapper.toDTO(savedProject);
    }

@Transactional
public ProjectResponseDTO updateProject(UUID id, ProjectRequestDTO projectDetails) {
    Project project = getProjectEntityById(id);

    if (projectDetails.title() != null) {
        project.setTitle(projectDetails.title());
    }
    if (projectDetails.lessonId() != null) {
        project.setLessonId(projectDetails.lessonId());
    }
    if (projectDetails.customInstructions() != null) {
        project.setCustomInstructions(projectDetails.customInstructions());
    }
    if (projectDetails.slideNumber() != null) {
        project.setSlideNum(projectDetails.slideNumber());
    }

    Project updatedProject = projectRepository.save(project);
    return projectMapper.toDTO(updatedProject);
}

    @Transactional
    public void deleteProject(UUID id) {
        Project project = getProjectEntityById(id);
        project.setDeleted(true);
        projectRepository.save(project);
    }

    @Transactional
    public ProjectResponseDTO updateLessonPlanFile(LessonPlanFileUploadDTO lessonPlanFileUploadDTO, UUID projectId) {
        Project project = getProjectEntityById(projectId);

        MultipartFile mediaFile = lessonPlanFileUploadDTO.getMediaFile();

        if (mediaFile == null || mediaFile.isEmpty()) {
            throw new BadRequestException("Media file cannot be null or empty");
        }

        LessonPlanFileDownloadDTO savedLectureFile = lectureMediaWebClientService.uploadLectureFile(lessonPlanFileUploadDTO);
        project.setLessonPlanFileId(savedLectureFile.getId());

        Project updatedProject = projectRepository.save(project);

        return projectMapper.toDTO(updatedProject);
    }

    public List<ProjectResponseDTO> getCurrentUserProjects() {
        UUID currentUserId = AuthenticationUtils.getUserId();
        List<Project> projects = projectRepository.findByUserIdAndIsDeletedIsFalse(currentUserId);
        if (projects.isEmpty()) {
            throw new NotFoundException(Constants.ErrorCode.PROJECT_NOT_FOUND);
        }

        return projects.stream()
                .map(projectMapper::toDTO)
                .collect(toList());
    }
}
