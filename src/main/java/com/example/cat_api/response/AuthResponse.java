package com.example.cat_api.response;

import com.example.cat_api.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String message;
    private Roles role;
}

