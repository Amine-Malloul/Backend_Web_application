package com.example.WebApplication.config;

import com.example.WebApplication.Security.AuthEntryPointJwt;
import com.example.WebApplication.Security.AuthTokenFilter;
import com.example.WebApplication.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    @Autowired
    AuthEntryPointJwt unauthorizedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and() // Enable Cross-Origin Resource Sharing (CORS)
                .csrf().disable() // Disable CSRF protection
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler).and() // Set the authentication entry point for unauthorized requests
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() // Set the session management policy to stateless
                .authorizeRequests()
                .antMatchers("/api/**").permitAll() // Permit all requests to the /api/** URL pattern
                .anyRequest().authenticated(); // Require authentication for any other request

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class); // Add the custom AuthTokenFilter before the UsernamePasswordAuthenticationFilter
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder()); // Configure the authentication manager with the user details service and password encoder
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Create an instance of BCryptPasswordEncoder as the password encoder
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(); // Create an instance of the custom AuthTokenFilter
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean(); // Expose the AuthenticationManager bean
    }
}
