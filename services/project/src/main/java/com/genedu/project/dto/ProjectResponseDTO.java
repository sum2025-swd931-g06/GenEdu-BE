package com.genedu.project.dto;

import com.genedu.project.model.enumeration.ProjectStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponseDTO {
    private UUID id;
    private UUID userId;
    private Long lessonId;
    private String title;
    private String lessonPlanFileUrl;
    private String customInstructions;
    private ProjectStatus status;
    private Integer slideNum;
    private Long templateId;
}
