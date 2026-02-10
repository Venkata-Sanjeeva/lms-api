package com.example.cat_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class CatApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatApiApplication.class, args);
		
//		Argon2PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
//		String result = encoder.encode("admin@123");
//		System.out.println("Encoded Password: " + result);
		/*
			Improving Performance with @Async
			Sending an email can take 2–5 seconds. If you call this from a controller, your API will feel "slow." To fix this, you can make the method run in the background.
			
			Add @EnableAsync to your main CatApiApplication class.
			
			Add @Async above your sendHtmlEmail method in EmailService.
			
			Now, the controller will return "Email sent" immediately while the actual work happens on a separate thread.
		 */
	}
}
