package com.genedu.project.dto;

import com.genedu.project.model.enumeration.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCreationDTO {
    @NotNull(message = "Lesson ID must not be null")
    private Long lessonId;

    @NotBlank(message = "Title must not be blank")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @NotNull(message = "Lesson plan file ID must not be null")
    private UUID lessonPlanFileId;

    private String customInstructions;

    private Integer slideNum;
}
