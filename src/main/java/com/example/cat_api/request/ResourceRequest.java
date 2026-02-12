package com.example.cat_api.request;

import com.example.cat_api.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResourceRequest {
    private ResourceType type;
    private String fileUrl;
}
