package com.genedu.content.dto.flatResponse;

import lombok.Builder;

@Builder
public record FlatSubjectMaterialDTO (
    Integer subjectId,
    String subjectName,
    String subjectDescription,

    Long materialId,
    String title,
    Integer orderNumber,
    String description
    ){}
