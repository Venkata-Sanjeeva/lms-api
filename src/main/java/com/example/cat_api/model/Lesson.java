package com.example.cat_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT") // Important for long content
    private String content;

    @Column(columnDefinition = "TEXT") // Important for code blocks
    private String codeExample;

    @Column(nullable = false)
    private Integer sequenceOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    @ToString.Exclude // Prevent infinite loops
    @JsonIgnore      // Prevent infinite recursion in JSON
    private Module module;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    // orphanRemoval = true ensures that Hibernate actually deletes that record from the database. Without it, the record might stay in the database with a null
    private List<Resource> resourceList = new ArrayList<>();
}