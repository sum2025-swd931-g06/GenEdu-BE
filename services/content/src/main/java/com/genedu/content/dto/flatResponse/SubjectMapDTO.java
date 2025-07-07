package com.genedu.content.dto.flatResponse;

import lombok.Builder;

@Builder
public record SubjectMapDTO(
        Integer subjectId,
        String subjectName,
        Integer schoolClassId
) {}
