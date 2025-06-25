package com.genedu.project.service;

import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.project.dto.ProjectRequestDTO;
import com.genedu.project.dto.ProjectResponseDTO;
import com.genedu.project.model.Project;
import com.genedu.project.model.enumeration.ProjectStatus;
import com.genedu.project.repository.ProjectRepository;
import com.genedu.project.utils.Constants;
import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.genedu.project.dto.client.LectureFileUploadDTO;
import com.genedu.project.dto.client.LectureFileDownloadDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final LectureMediaWebClientService lectureMediaWebClientService;

    public List<ProjectResponseDTO> getAllProjects() {
        List<Project> projects = projectRepository.findByIsDeletedIsFalse();
        List<ProjectResponseDTO> projectResponseDTOs = projects.stream()
                .map(project -> ProjectResponseDTO.builder()
                        .id(project.getId())
                        .title(project.getTitle())
                        .lessonId(project.getLessonId())
                        .slideNum(project.getSlideNum())
                        .status(project.getStatus())
                        .customInstructions(project.getCustomInstructions())
                        .userId(project.getUserId())
                        .lessonPlanFileUrl(
                                project.getLessonPlanFileId() != null
                                    ? lectureMediaWebClientService.getLessonPlanFileUrlByLessonPlanId(project.getLessonPlanFileId())
                                    : null
                        )
                        .build())
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

        List<ProjectResponseDTO> projectResponseDTOS = projects.stream()
                .map(project -> ProjectResponseDTO.builder()
                        .id(project.getId())
                        .title(project.getTitle())
                        .lessonId(project.getLessonId())
                        .slideNum(project.getSlideNum())
                        .status(project.getStatus())
                        .customInstructions(project.getCustomInstructions())
                        .userId(project.getUserId())
                        .lessonPlanFileUrl(
                                project.getLessonPlanFileId() != null
                                        ? lectureMediaWebClientService.getLessonPlanFileUrlByLessonPlanId(project.getLessonPlanFileId())
                                        : null
                        )
                        .build())
                .toList();
        return projectResponseDTOS;
    }

    public ProjectResponseDTO getProjectById(UUID id) {
        Optional<Project> project = projectRepository.findByIdAndIsDeletedIsFalse(id);
        if (project.isEmpty()) {
            throw new NotFoundException(Constants.ErrorCode.PROJECT_NOT_FOUND, id);
        }
        return ProjectResponseDTO.builder()
                .id(project.get().getId())
                .title(project.get().getTitle())
                .lessonId(project.get().getLessonId())
                .slideNum(project.get().getSlideNum())
                .status(project.get().getStatus())
                .customInstructions(project.get().getCustomInstructions())
                .userId(project.get().getUserId())
                .lessonPlanFileUrl(
                        project.get().getLessonPlanFileId() != null
                                ? lectureMediaWebClientService.getLessonPlanFileUrlByLessonPlanId(project.get().getLessonPlanFileId())
                                : null
                )
                .build();
    }

    @Transactional
    public ProjectResponseDTO createProject(ProjectRequestDTO projectDTO) {
        Project project = new Project();
        project.setUserId(AuthenticationUtils.getUserId());
        project.setLessonId(projectDTO.getLessonId());
        project.setTitle(projectDTO.getTitle());
        project.setCustomInstructions(projectDTO.getCustomInstructions());
        project.setSlideNum(projectDTO.getSlideNum() != null ? projectDTO.getSlideNum() : 10);
        project.setStatus(ProjectStatus.DRAFT);
        project.setDeleted(false);

        Project savedProject = projectRepository.save(project);

        return ProjectResponseDTO.builder()
                .id(savedProject.getId())
                .title(savedProject.getTitle())
                .lessonId(savedProject.getLessonId())
                .slideNum(savedProject.getSlideNum())
                .status(savedProject.getStatus())
                .customInstructions(savedProject.getCustomInstructions())
                .userId(savedProject.getUserId())
                .lessonPlanFileUrl(
                        savedProject.getLessonPlanFileId() != null
                                ? lectureMediaWebClientService.getLessonPlanFileUrlByLessonPlanId(savedProject.getLessonPlanFileId())
                                : null
                )
                .build();
    }

    @Transactional
    public ProjectResponseDTO updateProject(UUID id, ProjectRequestDTO projectDetails) {
        Project project = getProjectEntityById(id);

        // Update the project fields
        project.setTitle(projectDetails.getTitle());
        project.setCustomInstructions(projectDetails.getCustomInstructions());
        project.setSlideNum(projectDetails.getSlideNum());

        Project updatedProject = projectRepository.save(project);
        return ProjectResponseDTO.builder()
                .id(updatedProject.getId())
                .title(updatedProject.getTitle())
                .lessonId(updatedProject.getLessonId())
                .slideNum(updatedProject.getSlideNum())
                .status(updatedProject.getStatus())
                .customInstructions(updatedProject.getCustomInstructions())
                .userId(updatedProject.getUserId())
                .lessonPlanFileUrl(
                        updatedProject.getLessonPlanFileId() != null
                                ? lectureMediaWebClientService.getLessonPlanFileUrlByLessonPlanId(updatedProject.getLessonPlanFileId())
                                : null
                )
                .build();
    }

    @Transactional
    public void deleteProject(UUID id) {
        Project project = getProjectEntityById(id);
        project.setDeleted(true);
        projectRepository.save(project);
    }

    @Transactional
    public ProjectResponseDTO updateLessonPlanFile(LectureFileUploadDTO lectureFileUploadDTO) {
        Project project = getProjectEntityById(lectureFileUploadDTO.getProjectId());
        
        MultipartFile mediaFile = lectureFileUploadDTO.getMediaFile();

        if (mediaFile == null || mediaFile.isEmpty()) {
            throw new IllegalArgumentException("Media file cannot be null or empty");
        }

        LectureFileDownloadDTO savedLectureFile = lectureMediaWebClientService.uploadLectureFile(lectureFileUploadDTO);
        project.setLessonPlanFileId(savedLectureFile.getId());

        Project updatedProject = projectRepository.save(project);

        return ProjectResponseDTO.builder()
                .id(updatedProject.getId())
                .title(updatedProject.getTitle())
                .lessonId(updatedProject.getLessonId())
                .slideNum(updatedProject.getSlideNum())
                .status(updatedProject.getStatus())
                .customInstructions(updatedProject.getCustomInstructions())
                .userId(updatedProject.getUserId())
                .lessonPlanFileUrl(
                        updatedProject.getLessonPlanFileId() != null
                                ? lectureMediaWebClientService.getLessonPlanFileUrlByLessonPlanId(updatedProject.getLessonPlanFileId())
                                : null
                )
                .build();
    }

    public List<ProjectResponseDTO> getCurrentUserProjects() {
        UUID currentUserId = AuthenticationUtils.getUserId();
        List<Project> projects = projectRepository.findByUserIdAndIsDeletedIsFalse(currentUserId);
        if (projects.isEmpty()) {
            throw new NotFoundException(Constants.ErrorCode.PROJECT_NOT_FOUND);
        }

        return projects.stream()
                .map(project -> ProjectResponseDTO.builder()
                        .id(project.getId())
                        .title(project.getTitle())
                        .lessonId(project.getLessonId())
                        .slideNum(project.getSlideNum())
                        .status(project.getStatus())
                        .customInstructions(project.getCustomInstructions())
                        .userId(project.getUserId())
                        .lessonPlanFileUrl(
                                project.getLessonPlanFileId() != null
                                        ? lectureMediaWebClientService.getLessonPlanFileUrlByLessonPlanId(project.getLessonPlanFileId())
                                        : null
                        )
                        .build())
                .collect(toList());
    }
}
