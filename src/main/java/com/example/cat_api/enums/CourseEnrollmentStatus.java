package com.example.cat_api.enums;

public enum CourseEnrollmentStatus {
    PENDING,   // Awaiting payment or approval
    ACTIVE,    // Currently learning
    COMPLETED, // Finished all lessons
    CANCELLED, // User withdrew
    EXPIRED    // Access time ran out
}
