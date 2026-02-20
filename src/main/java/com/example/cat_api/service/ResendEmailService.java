package com.example.cat_api.service;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ResendEmailService {

    // This will pull the key from Render's Environment Variables
    @Value("${RESEND_API_KEY}")
    private String resendApiKey;

    public void sendCourseRegistrationEmail(String toEmail, String courseName) {
        Resend resend = new Resend(resendApiKey);

        CreateEmailOptions params = CreateEmailOptions.builder()
            .from("LMS Platform <onboarding@resend.dev>") // Use your verified domain once ready
            .to(toEmail)
            .subject("Successfully Registered: " + courseName)
            .html("<strong>Welcome!</strong><p>You have successfully registered for " + courseName + ".</p>")
            .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println("Email sent successfully! ID: " + data.getId());
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
    
    public String sendResetPasswordEmail(String toEmail, String subject, String emailContent) throws Exception {
    	Resend resend = new Resend(resendApiKey);

        CreateEmailOptions params = CreateEmailOptions.builder()
            .from("onboarding@resend.dev")
            .to(toEmail)
            .subject(subject)
            .html(emailContent)
            .build();

        CreateEmailResponse data = resend.emails().send(params);
        
        return data.getId();
    }
}