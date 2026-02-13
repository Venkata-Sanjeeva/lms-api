package com.example.cat_api.repository;

import com.example.cat_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserUID(String userUID);
    boolean existsByEmail(String email);
}
