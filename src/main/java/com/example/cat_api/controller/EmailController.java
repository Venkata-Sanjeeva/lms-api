package com.example.cat_api.controller;

import com.example.cat_api.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class EmailController {

    @Autowired
    private EmailService emailService;

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