package com.genedu.content.dto.flatResponse;

import lombok.Builder;

@Builder
public record SchoolClassMapDTO(
        Integer schoolClassId,
        String schoolClassName
){}
