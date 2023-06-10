package com.example.WebApplication.Controller;

import com.example.WebApplication.Repository.UserRepository;
import com.example.WebApplication.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/test")
public class TestController {

    private UserService userService;

    @Autowired
    public TestController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint accessible by all users (public access)
    @GetMapping("/all")
    public String allAccess(){
        return "Public Content.";
    }

    // Endpoint accessible only by users with the 'USER' role
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String studentAccess() {
        return "User Content.";
    }

    // Endpoint accessible only by users with the 'ADMIN' role
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    // Endpoint to verify user registration by providing a verification code
    @GetMapping("/verify")
    public String verifyUserRegistration(@RequestParam("code") String code){

        if (userService.verify(code)) {
            return "verify_success";
        }

        return "verify_fail";
    }
}
