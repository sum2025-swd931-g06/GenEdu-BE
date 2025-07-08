package com.genedu.lecturecontent.dto;

import jakarta.validation.constraints.NotBlank;

public record TextToSpeechRequestDTO(
        @NotBlank(message = "Text cannot be blank")
        String text
) {
}
