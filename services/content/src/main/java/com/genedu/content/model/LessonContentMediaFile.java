package com.genedu.content.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "lesson_content_media_files")
public class LessonContentMediaFile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lesson_content_media_files_id_gen")
    @SequenceGenerator(name = "lesson_content_media_files_id_gen", sequenceName = "lesson_content_media_files_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_content_id", nullable = false)
    private LessonContent lessonContent;

    @NotNull
    @Column(name = "media_file_id", nullable = false)
    private Long mediaFileId;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

}