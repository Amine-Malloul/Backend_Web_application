package com.example.WebApplication.Service;

import com.example.WebApplication.Model.Maintenance;
import com.example.WebApplication.Model.User;
import com.example.WebApplication.Model.UserDetailsImpl;
import com.example.WebApplication.Repository.MaintenanceRepository;
import com.example.WebApplication.Repository.UserRepository;
import com.example.WebApplication.Security.JwtUtils;
import com.example.WebApplication.payload.request.LoginRequest;
import com.example.WebApplication.payload.response.JwtResponse;
import com.example.WebApplication.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthentificationService {


    AuthenticationManager authenticationManager;
    JwtUtils jwtUtil;
    private final MaintenanceRepository maintenanceRepository;
    private final UserRepository userRepository;

    @Autowired
    public AuthentificationService(AuthenticationManager authenticationManager, JwtUtils jwtUtil, MaintenanceRepository maintenanceRepository, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.maintenanceRepository = maintenanceRepository;
        this.userRepository = userRepository;
    }


/**
     * Authenticates the user login request.
     *
     * @param loginRequest The login request containing username and password.
     * @return ResponseEntity containing the JWT response if successful, or an error message if authentication fails.
     */
    public ResponseEntity<?> authenticateLogin(@RequestBody LoginRequest loginRequest) {

        Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());

        if (! user.isPresent()) return ResponseEntity
                .badRequest().body(new MessageResponse("le compte avec ce nom d'utilisateur n'existe pas"));

        if ( ! user.get().isEnabled() ) return ResponseEntity
                .badRequest().body(new MessageResponse("Veuillez activer votre e-mail et réessayer !"));

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtil.generateJwtToken(authentication);



        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority()).collect(Collectors.toList());


        if ( ! isenabled() && !(roles.contains("ROLE_ADMIN")) ) return ResponseEntity
                .badRequest().body(new MessageResponse("Le site Web est actuellement en maintenance !"));


        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(),
                                userDetails.getUsername(), userDetails.getEmail(),
                                roles));
        }catch (AuthenticationException authException){
            return  ResponseEntity
                    .badRequest().body(new MessageResponse("le nom d'utilisateur ou le mot de passe est incorrect !"));
        }

    }



	/**
     * Checks if the maintenance mode is enabled.
     *
     * @return true if maintenance mode is enabled, false otherwise.
     */
    private boolean isenabled(){
        Optional<Maintenance> maintenance1 = maintenanceRepository.findById(1L) ;
        Maintenance maintenance = null;
        if (maintenance1.isPresent())  maintenance = maintenance1.get();

        if (maintenance.isEnabled()) return true;

        return false;
    }
}
