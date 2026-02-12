package com.example.cat_api.service;

import org.springframework.stereotype.Service;

import com.example.cat_api.exceptions.ModuleNotFoundException;
import com.example.cat_api.model.Lesson;
import com.example.cat_api.model.Module;
import com.example.cat_api.repository.LessonRepository;
import com.example.cat_api.repository.ModuleRepository;
import com.example.cat_api.request.CreateLessonRequest;
import com.example.cat_api.response.CreatedLessonResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final ModuleRepository moduleRepository;

    public CreatedLessonResponse addLessonToModule(Long moduleId, CreateLessonRequest request) {
        // 1. Verify Module exists
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ModuleNotFoundException("Module not found with id: " + moduleId));

        // 2. Create Lesson Entity
        Lesson lesson = new Lesson();
        lesson.setTitle(request.getTitle());
        lesson.setContent(request.getContent());
        lesson.setCodeExample(request.getCodeExample());
        lesson.setModule(module);

        // 3. Logic: Auto-increment sequenceOrder
        if (request.getSequenceOrder() == null) {
            int maxOrder = lessonRepository.findMaxSequenceOrderByModuleId(moduleId);
            lesson.setSequenceOrder(maxOrder + 1);
        } else {
            lesson.setSequenceOrder(request.getSequenceOrder());
        }

        Lesson savedLesson = lessonRepository.save(lesson);

        // 4. Return DTO
        return CreatedLessonResponse.builder()
                .id(savedLesson.getId())
                .title(savedLesson.getTitle())
                .content(savedLesson.getContent())
                .codeExample(savedLesson.getCodeExample())
                .sequenceOrder(savedLesson.getSequenceOrder())
                .moduleId(module.getId())
                .build();
    }
}