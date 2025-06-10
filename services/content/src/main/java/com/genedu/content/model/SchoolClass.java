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
@Table(name = "school_classes")
public class SchoolClass extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", columnDefinition = "varchar(255)")
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;
}
