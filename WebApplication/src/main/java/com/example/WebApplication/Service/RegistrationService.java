package com.example.WebApplication.Service;

import com.example.WebApplication.Model.ERole;
import com.example.WebApplication.Model.Role;
import com.example.WebApplication.Model.User;
import com.example.WebApplication.Repository.RoleRepository;
import com.example.WebApplication.payload.request.RegistrationRequest;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

@Service
public class RegistrationService {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Autowired
    public RegistrationService(UserService userService, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

/**
     * Registers a new user with the provided registration details.
     *
     * @param request  The registration request containing user details.
     * @param siteURL  The URL of the website.
     * @return ResponseEntity containing the response message if successful, or an error message if registration fails.
     * @throws MessagingException           if there's an error with sending the verification email.
     * @throws UnsupportedEncodingException if there's an error with encoding the email content.
     */
    public ResponseEntity<?> register(RegistrationRequest request, String siteURL) throws MessagingException, UnsupportedEncodingException {

        String randomCode = RandomString.make(64);

        String encodedPassword = passwordEncoder.
                encode(request.getPassword());

        User user = new User(request.getUsername(), request.getEmail(),
                encodedPassword, randomCode, false);


        Set<String> strRoles = request.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null ){
            Role role = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("role not found"));
            roles.add(role);
        } else{
            strRoles.forEach(role -> {
                switch (role){
                    case "admin":
                        Role adminrole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("role not found"));
                        roles.add(adminrole);
                        break;

                    default:
                        Role studentrole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("role not found"));
                        roles.add(studentrole);
                }

            });
        }

        user.setRoles(roles);

        sendVerificationCode( user, siteURL);

        return userService.signUpUser(user);
    }


 /**
     * Sends the verification email with a verification code to the user's email address.
     *
     * @param user     The user to whom the verification email will be sent.
     * @param siteURL  The URL of the website.
     * @throws MessagingException           if there's an error with sending the verification email.
     * @throws UnsupportedEncodingException if there's an error with encoding the email content.
     */

    private void sendVerificationCode(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {


        String fromAddress = "m2i.fso@gmail.com";
        
        String toAddress = user.getEmail();


        String subject = "Veuillez activer votre email";
        String content = "Cher " + user.getUsername() + ",<br>"
                + "Veuillez cliquer ci-dessous pour valider votre adresse email et pour activer votre compte.!<br>"
                + "<h2><a href=\"[[URL]]\" target=\"_self\">ACTIVER EMAIL</a></h2>"
                + "Si vous avez des questions, contactez nous en envoyant un email à " + fromAddress + "<br><br>"
                + "Cordialement,"
                +"M2I-FSO";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress , "M2I");
        helper.setTo(toAddress);
        helper.setSubject(subject);

        String verifyURL = "http://localhost:3000" + "/verify?code=" + user.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
        mailSender.send(message);
    }
}
