package com.example.cat_api.model;

import com.example.cat_api.enums.CourseEnrollmentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "course_enrollments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "course_id"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseEnrollmentStatus status; // Simplified name

    @Column(nullable = false, updatable = false)
    private LocalDateTime enrolledAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private User user; // Renamed from enrolledUsers for clarity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Course course;

    @PrePersist
    protected void onEnroll() {
        this.enrolledAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = CourseEnrollmentStatus.ACTIVE;
        }
    }
}