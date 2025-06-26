package com.genedu.project.model;

import com.genedu.commonlibrary.model.AbstractTimeAuditEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "slide_contents")
@Getter
@Setter
public class SlideContent extends AbstractTimeAuditEntity {
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
    @JoinColumn(name = "lecture_content_id", nullable = false)
    private LectureContent lectureContent;

    @Column(
            name = "order_number",
            nullable = false
    )
    private Integer orderNumber;

    @Column(
            name = "slide_title",
            columnDefinition = "VARCHAR(150)"
    )
    private String slideTitle;

    @Column(
            name = "main_idea",
            columnDefinition = "TEXT",
            nullable = false
    )
    private String mainIdea;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(
            name = "subpoints",
            columnDefinition = "jsonb",
            nullable = false
    )
    private Map<String, Object> subpoints;

    @Column(
            name = "narration_script",
            columnDefinition = "TEXT"
    )
    private String narrationScript;
}
