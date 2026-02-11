package com.example.cat_api.service;

import com.example.cat_api.exceptions.UserNotFoundException;
import com.example.cat_api.model.User;
import com.example.cat_api.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
    
    public User getUserByUniqueId(String uniqueId) {
    	return userRepo.findByUserUniqueId(uniqueId).orElseThrow(() -> new UserNotFoundException("User with " + uniqueId + " not found!"));
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with " + email + " not found!"));
    }

}
