package com.example.cat_api.controller;

import com.example.cat_api.request.ResetPasswordRequest;
import com.example.cat_api.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {


    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        try {
            emailService.processForgotPassword(email);
            // We return OK even if the email doesn't exist for security
            return ResponseEntity.ok("If an account exists for " + email + ", a reset link has been sent.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing request: " + e.getMessage());
        }
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            emailService.updatePassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok("Password has been successfully updated.");
        } catch (IllegalArgumentException e) {
            // This catches expired or invalid tokens
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred during password reset.");
        }
    }
    

    @GetMapping("/test-email")
    public ResponseEntity<String> sendTestEmail(@RequestParam String to) {
        String htmlContent = """
                <h1 style='color: #2e6c80;'>Welcome to Cat API!</h1>
                <p>This is a <strong>test email</strong> to verify our Spring Boot configuration.</p>
                <img src='https://http.cat/200' alt='cat' width='200'/>
                """;

        try {
            emailService.sendHtmlEmail(to, "Test Email from Spring Boot", htmlContent);
            return ResponseEntity.ok("Email sent successfully to: " + to);
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Error sending email: " + e.getMessage());
        }
    }
}