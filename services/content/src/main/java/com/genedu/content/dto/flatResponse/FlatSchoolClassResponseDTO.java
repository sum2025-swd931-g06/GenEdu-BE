package com.genedu.content.dto.flatResponse;

import java.util.List;

public record FlatSchoolClassResponseDTO(
        Integer schoolClassId,
        String schoolClassName,
        String schoolClassDescription,
        List<FlatSubjectChapterLessonDTO> subjectChapterLessonDTOList
) {}
