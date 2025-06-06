package com.genedu.content.model;

import jakarta.persistence.*;

@Entity
@Table(name = "chapters")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", columnDefinition = "int")
    private Integer orderNumber;

    @Column(name = "title", columnDefinition = "varchar(255)")
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Subject subject;
}
