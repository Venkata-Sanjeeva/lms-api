package com.example.cat_api.service;

import com.example.cat_api.enums.Roles;
import com.example.cat_api.exceptions.EmailAlreadyExistsException;
import com.example.cat_api.exceptions.InvalidLoginCredentialsException;
import com.example.cat_api.exceptions.UserNotFoundException;
import com.example.cat_api.model.User;
import com.example.cat_api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with " + email + " not found!"));
    }

    public User registerUser(String name, String email, String password) {

        if (userRepo.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email + " already exists in DB");
        }

        User user = new User();
        user.setUserUniqueId("USER-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        user.setName(name);
        user.setEmail(email);

        // ✅ Hash password using Argon2
        user.setPasswordHash(passwordEncoder.encode(password));

        user.setRole(Roles.USER);

        return userRepo.save(user);
    }

    public boolean verifyUser(String userEmail, String userPassword) {
        User user = userRepo.findByEmail(userEmail).orElseThrow(() -> new InvalidLoginCredentialsException("Invalid email or password"));
        return passwordEncoder.matches(userPassword, user.getPasswordHash());
    }

}
