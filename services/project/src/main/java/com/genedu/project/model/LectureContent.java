package com.genedu.project.model;

import com.genedu.commonlibrary.model.AbstractTimeAuditEntity;
import com.genedu.project.model.enumeration.LectureStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lecture_contents")
@Getter
@Setter
public class LectureContent extends AbstractTimeAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(
            name = "id",
            columnDefinition = "uuid",
            updatable = false,
            nullable = false
    )
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(
            name = "version",
            columnDefinition = "VARCHAR(20) default 'v1'"
    )
    private String version;

    @Column(
            name = "title",
            columnDefinition = "VARCHAR(150)",
            nullable = false
    )
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            columnDefinition = "VARCHAR(20) default 'DRAFT'",
            nullable = false
    )
    private LectureStatus status;

    @OneToMany(mappedBy = "lectureContent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SlideContent> slideContents = new ArrayList<>();

    @OneToOne(mappedBy = "lectureContent", cascade = CascadeType.ALL, orphanRemoval = true)
    private FinalizedLecture finalizedLecture;
}
