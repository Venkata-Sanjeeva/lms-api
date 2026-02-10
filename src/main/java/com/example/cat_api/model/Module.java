package com.example.cat_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "modules")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

}
