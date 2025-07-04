package com.genedu.lecturecontent.model;

import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class LessonPlanTemplate {
    private String lessonTitle;
    private List<String> objectives;
    private List<ActivityPlan> activities;
}
