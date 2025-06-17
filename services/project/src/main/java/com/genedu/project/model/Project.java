package com.genedu.project.model;

import com.genedu.commonlibrary.model.AbstractAuditEntity;
import com.genedu.project.model.enumeration.ProjectStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "projects")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Project extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(
            name = "id",
            columnDefinition = "uuid",
            updatable = false,
            nullable = false
    )
    private UUID id;

    @Column(
            name = "user_id",
            columnDefinition = "uuid",
            nullable = false
    )
    private UUID userId;

    @Column(
            name = "lesson_id",
            columnDefinition = "bigint",
            nullable = false
    )
    private Long lessonId;

    @Column(
            name = "title",
            columnDefinition = "VARCHAR(255)",
            nullable = false
    )
    private String title;

    @Column(
            name = "lesson_plan_file_id",
            columnDefinition = "uuid",
            nullable = false
    )
    private UUID lessonPlanFileId;

    @Column(
            name = "custom_instructions",
            columnDefinition = "TEXT"
    )
    private String customInstructions;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            columnDefinition = "VARCHAR(20) default 'DRAFT'",
            nullable = false
    )
    private ProjectStatus status;

    @Column(
            name = "slide_num",
            columnDefinition = "int default 10"
    )
    private Integer slideNum;

    @Column(
            name = "template_id",
            columnDefinition = "bigint"
    )
    private Long templateId;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LectureContent> lectureContents = new ArrayList<>();

}
