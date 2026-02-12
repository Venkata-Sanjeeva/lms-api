package com.example.cat_api.service;

import org.springframework.stereotype.Service;

import com.example.cat_api.exceptions.CourseNotFoundException;
import com.example.cat_api.model.Course;
import com.example.cat_api.model.Module;
import com.example.cat_api.repository.CourseRepository;
import com.example.cat_api.repository.ModuleRepository;
import com.example.cat_api.request.CreateModuleRequest;
import com.example.cat_api.response.CreatedModuleResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;

    public CreatedModuleResponse addModuleToCourse(Long courseId, CreateModuleRequest request) {
        // 1. Find the parent course
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        // 2. Create the entity
        Module module = new Module();
        module.setTitle(request.getTitle());
        module.setCourse(course);

        // 3. Auto-calculate sequence if not provided
        if (request.getSequenceOrder() == null) {
            int maxOrder = moduleRepository.findMaxSequenceOrderByCourseId(courseId);
            module.setSequenceOrder(maxOrder + 1);
        } else {
            module.setSequenceOrder(request.getSequenceOrder());
        }

        Module savedModule = moduleRepository.save(module);

        // 4. Return the DTO
        return CreatedModuleResponse.builder()
                .title(savedModule.getTitle())
                .sequenceOrder(savedModule.getSequenceOrder())
                .courseUniqueId(course.getCourseUniqueId())
                .lessonCount(0)
                .build();
    }
}