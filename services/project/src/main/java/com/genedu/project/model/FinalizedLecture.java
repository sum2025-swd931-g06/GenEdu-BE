package com.genedu.project.model;

import com.genedu.commonlibrary.model.AbstractTimeAuditEntity;
import com.genedu.project.model.enumeration.PublishedStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "finalized_lectures")
@Getter
@Setter
public class FinalizedLecture extends AbstractTimeAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(
            name = "id",
            columnDefinition = "uuid",
            updatable = false,
            nullable = false
    )
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_content_id", nullable = false)
    private LectureContent lectureContent;

    @Column(
            name = "audio_file_id",
            columnDefinition = "uuid"
    )
    private UUID audioFileId;

    @Column(
            name = "presentation_file_id",
            columnDefinition = "uuid"
    )
    private UUID presentationFileId;

    @Column(
            name = "video_file_id",
            columnDefinition = "uuid"
    )
    private UUID videoFileId;

    @Column(
            name = "thumbnail_file_id",
            columnDefinition = "uuid"
    )
    private UUID thumbnailFileId;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "published_status",
            columnDefinition = "VARCHAR(20) default 'PRIVATE'",
            nullable = false
    )
    private PublishedStatus publishedStatus;
}
