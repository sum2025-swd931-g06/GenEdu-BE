package com.genedu.project.service.impl;

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
import com.genedu.project.service.ProjectService;
import com.genedu.project.utils.Constants;
import com.genedu.project.webclient.LectureMediaWebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final LectureMediaWebClientService lectureMediaWebClientService;
    private final ProjectMapper projectMapper;


    public Project getProjectEntityById(UUID id) {
        return projectRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.PROJECT_NOT_FOUND, id));
    }

    @Override
    public Page<ProjectResponseDTO> getProjectsByUserId(UUID userId, Pageable pageable) {
        Page<Project> projects = projectRepository.findByUserIdAndDeletedIsFalse(userId, pageable);
        return projects.map(projectMapper::toDTO);
    }

    public ProjectResponseDTO getProjectById(UUID id) {
        Optional<Project> project = projectRepository.findByIdAndDeletedIsFalse(id);
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
    public ProjectResponseDTO updateLessonPlanFile(LessonPlanFileUploadDTO lessonPlanFileUploadDTO) {
        UUID projectId = UUID.fromString(lessonPlanFileUploadDTO.getProjectId());
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

    public LessonPlanFileDownloadDTO getLessonPlanTemplate() {
        LessonPlanFileDownloadDTO lessonPlanTemplate = lectureMediaWebClientService.getLessonPlanTemplate();
        if (lessonPlanTemplate == null) {
            throw new NotFoundException(Constants.ErrorCode.LESSON_PLAN_TEMPLATE_NOT_FOUND);
        }

        return lessonPlanTemplate;
    }
}
