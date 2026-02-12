package com.example.cat_api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateModuleRequest {
    private String title;
    
    // Optional: If the admin wants to override the order, 
    // otherwise the backend can auto-calculate it.
    private Integer sequenceOrder; 
}