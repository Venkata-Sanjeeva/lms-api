package com.example.cat_api.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
