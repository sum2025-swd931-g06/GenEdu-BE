package com.genedu.content.dto.flatResponse;

import lombok.Builder;

import java.util.List;

@Builder
public record FlatSchoolClassResponseDTO<T>(
        Integer schoolClassId,
        String schoolClassName,
        String schoolClassDescription,
        List<T> subjects
) {}
