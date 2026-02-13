package com.example.cat_api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProgressRequest {
	private String courseUID;
    private String lessonUID;
    
    // Optional: Use this if you want one endpoint to toggle 
    // between completed (true) and started (false)
    private boolean completed; 
}