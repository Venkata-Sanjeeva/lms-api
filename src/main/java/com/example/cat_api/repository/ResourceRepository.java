package com.example.cat_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cat_api.enums.ResourceType;
import com.example.cat_api.model.Lesson;
import com.example.cat_api.model.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long>{
	List<Resource> findByLesson(Lesson lesson);
    List<Resource> findByResourceType(ResourceType type);
}
