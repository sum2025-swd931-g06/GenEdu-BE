package com.genedu.lecturecontent.dto;

import java.util.UUID;

public record ProjectResponseDTO(
     UUID id,
     UUID userId,
     Long lessonId,
     String title,
     String lessonPlanFileUrl,
     String customInstructions,
     Integer slideNum,
     Long templateId
) {
}
