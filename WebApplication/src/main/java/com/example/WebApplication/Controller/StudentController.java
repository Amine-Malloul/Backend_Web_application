package com.example.WebApplication.Controller;

import com.example.WebApplication.Model.Student;
import com.example.WebApplication.Service.StudentService;
import com.example.WebApplication.config.FileUploadUtil;
import com.example.WebApplication.payload.request.StudentInforequest;
import com.example.WebApplication.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/api/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Endpoint to get student information by user ID
    @GetMapping(path = "/{user_id}")
    public ResponseEntity<?> getStudentInfo(@PathVariable("user_id") Long id){
        return studentService.getStudentInfo(id);
    }

    // Endpoint to update student information and profile image
    @PutMapping(path = "/{user_id}")
    public ResponseEntity<?> updateStudentInfo(@RequestPart("studentInforequest") StudentInforequest studentInforequest,
                                               @PathVariable("user_id") Long id,
                                               @RequestPart("image") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Student student = studentService.updateStudentInfo(studentInforequest, id, fileName);
        String uploadDir = "src/main/resources/student_photos/" + student.getStudent_id();
        FileUploadUtil.saveFileinDir(uploadDir, fileName, multipartFile);

        return ResponseEntity.ok(new MessageResponse("Student has been modified successfully!"));
    }

    // Endpoint to update student information without updating the profile image
    @PutMapping("/uploadInfo/{user_id}")
    public ResponseEntity<?> updateStudent(@PathVariable("user_id") Long id,
                                           @RequestBody StudentInforequest studentInforequest) throws IOException {
        Student student = studentService.updateStudentInfo(studentInforequest, id, "");

        return ResponseEntity.ok(new MessageResponse("Les informations sont modifiées avec succès !"));
    }

    // Endpoint to upload a new profile image for the student
    @PutMapping("/uploadImage/{user_id}")
    public ResponseEntity<?> uploadimage(@PathVariable("user_id") Long id,
                                         @RequestPart("image") MultipartFile multipartFile) throws IOException {
        studentService.uploadImage(id, multipartFile);

        return ResponseEntity.ok(new MessageResponse("Les informations sont modifiées avec succès !"));
    }

    // Endpoint to upload a personal image for the student
    @PutMapping("/uploadImagePerso/{user_id}")
    public ResponseEntity<?> uploadPersoImage(@PathVariable("user_id") Long id,
                                              @RequestPart("persoimage") MultipartFile multipartFile) throws IOException {
        studentService.uploadPersoImage(id, multipartFile);

        return ResponseEntity.ok(new MessageResponse("La photo du profile est changée avec succès !"));
    }

    // Endpoint to get the personal image of the student
    @GetMapping("/PersoImage/{user_id}")
    public ResponseEntity<?> getPersoImage(@PathVariable("user_id") Long id) throws IOException {
        return studentService.getPersoImage(id);
    }

    // Endpoint to get the student's notes by user ID
    @GetMapping(path = "/note/{user_id}")
    public ResponseEntity<?> getStudentNote(@PathVariable("user_id") Long id){
        return studentService.getStudentNote(id);
    }
}
