package com.genedu.content.dto.flatResponse;

import lombok.Builder;

@Builder
public record MaterialMapDTO (
        Long materialId,
        String materialTitle,
        Integer subjectId) {
}
