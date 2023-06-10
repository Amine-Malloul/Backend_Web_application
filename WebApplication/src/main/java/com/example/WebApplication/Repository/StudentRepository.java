package com.example.WebApplication.Repository;

import com.example.WebApplication.Model.Student;
import com.example.WebApplication.Model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

// Find a Student entity by its associated User
    Optional<Student> findByuser(User user);
}
