package com.genedu.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequestDTO {
    @NotNull(message = "Lesson ID must not be null")
    private Long lessonId;

    @NotBlank(message = "Title must not be blank")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    private String customInstructions;

    private Integer slideNum;
}
