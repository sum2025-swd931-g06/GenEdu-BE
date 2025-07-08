package com.genedu.content.dto.flatResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Setter
@Getter
public class SchoolDataMapResponse {
    private Map<Integer, SchoolClassMapDTO> schoolClasses;
    private Map<Integer, SubjectMapDTO> subjects;
    private Map<Long, MaterialMapDTO> materials;
    private Map<Long, LessonMapDTO> lessons;
}
