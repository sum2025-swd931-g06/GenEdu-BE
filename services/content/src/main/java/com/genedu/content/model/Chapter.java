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
@Table(name = "chapters")
public class Chapter extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chapters_id_gen")
    @SequenceGenerator(name = "chapters_id_gen", sequenceName = "chapters_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id")
    private Material material;

    @Size(max = 150)
    @NotNull
    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @NotNull
    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

}