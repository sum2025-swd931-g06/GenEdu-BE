package com.genedu.content.model;


import com.genedu.commonlibrary.model.AbstractAuditEntity;
import jakarta.persistence.*;
import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "lessons")
public class Lesson extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", columnDefinition = "varchar(255)", nullable = false)
    private String title;

    @Column(name = "order_number", columnDefinition = "int", nullable = false)
    private Integer orderNumber;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Chapter chapter;
}
