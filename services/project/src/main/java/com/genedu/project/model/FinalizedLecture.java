package com.genedu.project.model;

import com.genedu.commonlibrary.model.AbstractTimeAuditEntity;
import com.genedu.project.model.enumeration.PublishedStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "finalized_lectures")
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
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
            columnDefinition = "bigint"
    )
    private Long audioFileId;

    @Column(
            name = "presentation_file_id",
            columnDefinition = "bigint"
    )
    private Long presentationFileId;

    @Column(
            name = "video_file_id",
            columnDefinition = "bigint"
    )
    private Long videoFileId;

    @Column(
            name = "thumbnail_file_id",
            columnDefinition = "bigint"
    )
    private Long thumbnailFileId;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "published_status",
            columnDefinition = "VARCHAR(20) default 'PRIVATE'",
            nullable = false
    )
    private PublishedStatus publishedStatus;
}
