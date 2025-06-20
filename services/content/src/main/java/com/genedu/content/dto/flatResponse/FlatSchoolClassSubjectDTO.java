package com.genedu.content.dto.flatResponse;

import lombok.Builder;

@Builder
public record FlatSchoolClassSubjectDTO(
        Integer schoolClassId,
        String schoolClassName,
        String schoolClassDescription,

        Integer subjectId,
        String subjectName,
        String subjectDescription
) {
}
