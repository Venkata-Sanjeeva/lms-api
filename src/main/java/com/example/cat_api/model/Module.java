package com.example.cat_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "modules")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String moduleUID; // Unique identifier for the module

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer sequenceOrder;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading is better for performance
    // LAZY tells Hibernate: "Don't grab the Course details until I actually call module.getCourse().getTitle()."
    @JoinColumn(name = "course_id", nullable = false) // Points to the DB column name
    @JsonIgnore
    @ToString.Exclude  // <--- This stops the infinite loop!
    private Course course;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

}
