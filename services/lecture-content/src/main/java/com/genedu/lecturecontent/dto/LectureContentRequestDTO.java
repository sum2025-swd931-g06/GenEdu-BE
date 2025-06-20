package com.genedu.lecturecontent.dto;

public class LectureContentRequestDTO {
    private String schoolClassId;
    private String subjectId;
    private String materialId;
    private String lessonId;
    private String lessonPartId;
    private String content;

    public LectureContentRequestDTO(String schoolClassId, String subjectId, String materialId, String lessonId, String lessonPartId, String content) {
        this.schoolClassId = schoolClassId;
        this.subjectId = subjectId;
        this.materialId = materialId;
        this.lessonId = lessonId;
        this.lessonPartId = lessonPartId;
        this.content = content;
    }

    public String getSchoolClassId() {
        return schoolClassId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public String getLessonPartId() {
        return lessonPartId;
    }

    public String getContent() {
        return content;
    }

    public void setSchoolClassId(String schoolClassId) {
        this.schoolClassId = schoolClassId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public void setLessonPartId(String lessonPartId) {
        this.lessonPartId = lessonPartId;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
