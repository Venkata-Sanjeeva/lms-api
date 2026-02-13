package com.example.cat_api.service;

import com.example.cat_api.enums.Roles;
import com.example.cat_api.exceptions.EmailAlreadyExistsException;
import com.example.cat_api.exceptions.InvalidLoginCredentialsException;
import com.example.cat_api.exceptions.UserNotFoundException;
import com.example.cat_api.model.User;
import com.example.cat_api.repository.UserRepository;
import com.example.cat_api.utils.IdentifierGenerator;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterAndLoginService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public RegisterAndLoginService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with " + email + " not found!"));
    }

    public User registerUser(String name, String email, String password, String role) {

        if (userRepo.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email + " already exists in DB");
        }

        User user = new User();
        
        String roleUC = role.toUpperCase();

        user.setUserUID(IdentifierGenerator.generate(roleUC));
        user.setName(name);
        user.setEmail(email);

        // ✅ Hash password using Argon2
        user.setPasswordHash(passwordEncoder.encode(password));

        if(roleUC.equals("USER")) {
        	user.setRole(Roles.USER);
        } else {
        	user.setRole(Roles.ADMIN);
        }

        return userRepo.save(user);
    }

    public boolean verifyUser(String userEmail, String userPassword) {
        User user = userRepo.findByEmail(userEmail).orElseThrow(() -> new InvalidLoginCredentialsException("Invalid email or password"));
        return passwordEncoder.matches(userPassword, user.getPasswordHash());
    }

}
