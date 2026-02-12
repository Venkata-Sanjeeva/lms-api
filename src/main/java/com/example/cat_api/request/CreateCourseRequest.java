package com.example.cat_api.request;

import lombok.Data;

@Data
public class CreateCourseRequest {
	private String title;
	private String description;
	private String difficulty;
	private String language;
}
