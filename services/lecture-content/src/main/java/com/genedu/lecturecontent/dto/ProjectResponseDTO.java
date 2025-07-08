package com.genedu.lecturecontent.dto;

import java.util.UUID;

public record ProjectResponseDTO(
     UUID id,
     Long lessonId,
     String title,
     String customInstructions,
     Integer slideNum,
     Long templateId
) {
}
