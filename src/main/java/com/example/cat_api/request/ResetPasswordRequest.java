package com.example.cat_api.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
	private String token;
	private String newPassword;
}
