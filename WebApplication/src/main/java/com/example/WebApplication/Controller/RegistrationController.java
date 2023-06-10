package com.example.WebApplication.Controller;

import com.example.WebApplication.Service.RegistrationService;
import com.example.WebApplication.payload.request.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping(path = "api/registration")
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

// Endpoint for user registration
    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request,
                                      HttpServletRequest httpServletRequest) throws MessagingException, UnsupportedEncodingException {


// Get the base URL of the application
        String siteUrl = httpServletRequest.getRequestURL().toString();
        return registrationService.register(request, siteUrl);
    }

}
