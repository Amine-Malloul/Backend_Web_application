package com.example.WebApplication.Controller;


import com.example.WebApplication.Model.ERole;
import com.example.WebApplication.Model.Role;
import com.example.WebApplication.Model.User;
import com.example.WebApplication.Repository.RoleRepository;
import com.example.WebApplication.Repository.UserRepository;
import com.example.WebApplication.Security.JwtUtils;
import com.example.WebApplication.Service.AuthentificationService;
import com.example.WebApplication.Service.RoleService;
import com.example.WebApplication.Service.UserService;
import com.example.WebApplication.payload.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping(path ="/api/auth" )
public class AuthController {

    private final AuthentificationService authentificationService;




    @Autowired
    public AuthController(AuthentificationService authentificationService) {
        this.authentificationService = authentificationService;
    }

// Endpoint for authenticating user login
    @PostMapping(path = "/signin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateLogin(@RequestBody LoginRequest loginRequest){
        return authentificationService.authenticateLogin(loginRequest);
    }



}
