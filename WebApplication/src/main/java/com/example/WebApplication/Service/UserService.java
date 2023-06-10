package com.example.WebApplication.Service;


import com.example.WebApplication.Model.*;
import com.example.WebApplication.Repository.UserRepository;
import com.example.WebApplication.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final StudentService studentService;

    @Autowired
    public UserService(UserRepository userRepository, StudentService studentService) {
        this.userRepository = userRepository;
        this.studentService = studentService;
    }


/**
     * Loads the user by username for authentication and authorization purposes.
     *
     * @param username the username of the user to load
     * @return UserDetails object representing the user
     * @throws UsernameNotFoundException if the user with the specified username is not found
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("utilisateur avec pseudo "+username+" non trouvé"));

        if ( !user.isEnabled() ) return null;

        return UserDetailsImpl.build(user);
    }


/**
     * Signs up a new user.
     *
     * @param user the user to sign up
     * @return ResponseEntity containing a success message if the user is signed up successfully,
     *         or a bad request message if the email or username is already taken
     */
    public ResponseEntity<?> signUpUser(User user){
        boolean userEmailExists = userRepository
                .findByEmail(user.getEmail()).isPresent();
        if(userEmailExists){
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Cet email est déjà occupé!"));
        }

        boolean userNameExists = userRepository
                .findByUsername(user.getUsername()).isPresent();
        if (userNameExists){
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Ce pseudo est déjà occupé"));
        }


        userRepository.save(user);
        Student student = new Student(user);
        studentService.add(student);

        //TODO: Send configuration taken

        return ResponseEntity.ok(new MessageResponse("vous êtes inscrit avec succès!"));
    }


/**
     * Verifies the user with the specified verification code.
     *
     * @param code the verification code
     * @return true if the user is verified and enabled, false otherwise
     */
    @Transactional
    public boolean verify(String code) {

        User user = userRepository.findByVerificationCode(code);

        if (user == null || user.isEnabled()) return false;

        user.setVerificationCode(null);
        user.setEnabled(true);

        return true;
    }


//    public List<User> getallUsers(){
//        return userRepository.findAll();
//    }

//    @Transactional
//    public void addUser(User user) {
//        Role role = roleRepository.findByName(ERole.ROLE_STUDENT);
//        user.setRole(role);
//        userRepository.save(user);
//    }


}
