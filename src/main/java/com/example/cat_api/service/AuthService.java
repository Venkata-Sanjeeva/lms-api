package com.example.cat_api.service;

import com.example.cat_api.exceptions.EmailAlreadyExistsException;
import com.example.cat_api.exceptions.InvalidLoginCredentialsException;
import com.example.cat_api.exceptions.UserNotFoundException;
import com.example.cat_api.model.User;
import com.example.cat_api.request.LoginRequest;
import com.example.cat_api.request.RegisterRequest;
import com.example.cat_api.response.AuthResponse;
import com.example.cat_api.security.JwtUtil;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final RegisterAndLoginService registerAndLoginService;
    private final JwtUtil jwtUtil;

    public AuthService(RegisterAndLoginService registerAndLoginService, JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.registerAndLoginService = registerAndLoginService;
    }

    // ✅ Register
    public AuthResponse register(RegisterRequest request, String role) throws EmailAlreadyExistsException {

        try {
            User savedUser = registerAndLoginService.registerUser(request.getName(), request.getEmail(), request.getPassword(), role);

            String token = jwtUtil.generateTokenUsingEmail(savedUser.getEmail());

            return new AuthResponse(token, "User Registered Successfully", savedUser.getRole());
        } catch (EmailAlreadyExistsException existsException) {
            throw new EmailAlreadyExistsException(existsException.getMessage());
        }
    }

    // ✅ Login
    public AuthResponse login(LoginRequest request) throws InvalidLoginCredentialsException, UserNotFoundException {
        // ✅ verify password
        try {
            boolean isPasswordVerified = registerAndLoginService.verifyUser(request.getEmail(), request.getPassword());

            if (isPasswordVerified) {
                User verifiedUser = registerAndLoginService.getUserByEmail(request.getEmail());


                String token = jwtUtil.generateTokenUsingEmailAndRole(request.getEmail(), verifiedUser.getRole());

                return new AuthResponse(token, "Login Successful", verifiedUser.getRole());
            }
        } catch (InvalidLoginCredentialsException loginCredentialsException) {
            throw new InvalidLoginCredentialsException(loginCredentialsException.getMessage());
        } catch (UserNotFoundException userNotFoundEx) {
            throw new UserNotFoundException(userNotFoundEx.getMessage());
        }

        throw new InvalidLoginCredentialsException("Invalid email or password");
    }
}
