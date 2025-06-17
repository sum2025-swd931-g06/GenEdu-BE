package com.genedu.content.model;

import com.genedu.commonlibrary.model.AbstractAuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "lessons")
public class Lesson extends AbstractAuditEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @Size(max = 150)
    @NotNull
    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @NotNull
    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 20)
    @ColumnDefault("'UN_SYNC'")
    @Column(name = "status", length = 20)
    private String status;
}