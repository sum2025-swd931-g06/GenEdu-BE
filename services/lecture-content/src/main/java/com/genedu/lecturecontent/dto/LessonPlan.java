package com.genedu.lecturecontent.dto;

import java.util.List;

// DTO Definitions for the Lesson Plan Structure
public record LessonPlan(String title, List<String> objectives, List<Activity> activities) {}

