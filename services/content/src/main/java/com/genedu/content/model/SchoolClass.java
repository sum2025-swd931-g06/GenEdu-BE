package com.genedu.content.model;

import com.genedu.commonlibrary.model.AbstractAuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "school_classes")
public class SchoolClass extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "school_classes_id_gen")
    @SequenceGenerator(name = "school_classes_id_gen", sequenceName = "school_classes_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;
}