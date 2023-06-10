package com.example.WebApplication.payload.request;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class LoginRequest {

    private String username; // The username provided in the login request
    private String password; // The password provided in the login request
}
