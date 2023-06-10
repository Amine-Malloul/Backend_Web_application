package com.example.WebApplication.Repository;

import com.example.WebApplication.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

     // Find a User entity by email
    Optional<User> findByEmail(String email);

    // Find a User entity by username
    Optional<User> findByUsername(String username);

    // Find a User entity by verification code
    User findByVerificationCode(String code);
}
