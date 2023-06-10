package com.example.WebApplication.Service;


import com.example.WebApplication.Model.Student;
import com.example.WebApplication.Model.User;
import com.example.WebApplication.Repository.StudentRepository;
import com.example.WebApplication.Repository.UserRepository;
import com.example.WebApplication.config.FileUploadUtil;
import com.example.WebApplication.payload.request.StudentInforequest;
import com.example.WebApplication.payload.response.MessageResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.aspectj.weaver.JoinPointSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;


/**
 * Service class for managing student-related operations.
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }
/**
     * Updates the information of a student.
     *
     * @param studentInforequest the request object containing the updated student information
     * @param id                 the ID of the student
     * @param fileName           the file name of the uploaded photo
     * @return the updated student object
     * @throws IOException if an I/O error occurs
     */
    @Transactional
    public Student updateStudentInfo(StudentInforequest studentInforequest,
                                               Long id, String fileName) throws IOException{

        Student upstudent = getcurrentstudent(id);
        upstudent.setLicence_photo(fileName);

// Updating the student information
        upstudent.setName(studentInforequest.getName());
        upstudent.setLastname(studentInforequest.getLastname());
        upstudent.setCin(studentInforequest.getCin());

        upstudent.setNationality(studentInforequest.getNationality());
        upstudent.setDob(studentInforequest.getDob());
        upstudent.setAddress(studentInforequest.getAddress());
        upstudent.setPhone(studentInforequest.getPhone());
        upstudent.setProfession_state(studentInforequest.getProfession_state());

        upstudent.setBac_year(studentInforequest.getBac_year());
        upstudent.setLicencetype(studentInforequest.getLicencetype());
        upstudent.setUni(studentInforequest.getUni());
        upstudent.setFac(studentInforequest.getFac());
        upstudent.setFirst_year(studentInforequest.getFirst_year());
        upstudent.setLicence_year(studentInforequest.getLicence_year());


        upstudent.setNotes1(studentInforequest.getNotes1());
        upstudent.setNotes2(studentInforequest.getNotes2());
        upstudent.setNotes3(studentInforequest.getNotes3());
        upstudent.setNotes4(studentInforequest.getNotes4());
        upstudent.setNotes5(studentInforequest.getNotes5());
        upstudent.setNotes6(studentInforequest.getNotes6());

        upstudent.setIsnormals1(studentInforequest.isIsnormals1());
        upstudent.setIsnormals2(studentInforequest.isIsnormals2());
        upstudent.setIsnormals3(studentInforequest.isIsnormals3());
        upstudent.setIsnormals4(studentInforequest.isIsnormals4());
        upstudent.setIsnormals5(studentInforequest.isIsnormals5());
        upstudent.setIsnormals6(studentInforequest.isIsnormals6());

        return upstudent;
    }

/**
     * Uploads the image of a student.
     *
     * @param id            the ID of the student
     * @param multipartFile the multipart file containing the image
     * @throws IOException if an I/O error occurs
     */
    @Transactional
    public void uploadImage(Long id, MultipartFile multipartFile) throws IOException {

        Student upstudent = getcurrentstudent(id);

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        upstudent.setLicence_photo(fileName);

        String uploadDir = "src/main/resources/student_photos/"+ upstudent.getStudent_id();
        FileUploadUtil.saveFileinDir(uploadDir, fileName, multipartFile);

    }


 /**
     * Uploads the personal image of a student.
     *
     * @param id            the ID of the student
     * @param multipartFile the multipart file containing the image
     * @throws IOException if an I/O error occurs
     */
    @Transactional
    public void uploadPersoImage(Long id, MultipartFile multipartFile) throws IOException {

        Student student = getcurrentstudent(id);

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        student.setPersonal_photo(fileName);

        String uploadDir = "src/main/resources/photos/student_photos/"+ student.getStudent_id();
        FileUploadUtil.saveFileinDir(uploadDir, fileName, multipartFile);

    }


  /**
     * Retrieves the personal image of a student.
     *
     * @param id the ID of the student
     * @return the ResponseEntity containing the image as a byte array
     * @throws IOException if an I/O error occurs
     */
    public ResponseEntity<?> getPersoImage(Long id) throws IOException {

        Student student = getcurrentstudent(id);

        Path path = Paths.get("src/main/resources/photos/student_photos/" + student.getStudent_id())
                .toAbsolutePath()
                .normalize();

        Path nullpath = Paths.get("src/main/resources/photos/student_photos/nullphotodir")
                .toAbsolutePath()
                .normalize();

        Path nullfile = nullpath.resolve("malenullphoto.png").normalize();

        Resource resource;

        if ( student.getPersonal_photo() == null || student.getPersonal_photo().equals("") ) {
            resource = new UrlResource(nullfile.toUri());
        }else{
            Path file = path.resolve(student.getPersonal_photo()).normalize();

            resource = new UrlResource(file.toUri());
        }

        byte[] imageBytes = StreamUtils.copyToByteArray(resource.getInputStream());

//        else {
////            return ResponseEntity.badRequest().body(new MessageResponse("Could not find file"));
//
//        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageBytes);

    }



/**
     * Adds a student to the repository.
     *
     * @param student the student to be added
     */
    public void add(Student student) {
        studentRepository.save(student);
    }


/**
     * Retrieves the current student based on the ID.
     *
     * @param id the ID of the student
     * @return the current student object
     */
    public Student getcurrentstudent(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow( () -> new IllegalArgumentException("Utilisateur non trouvé par id"));

        Optional<Student> student = studentRepository.findByuser(user);
        if (!student.isPresent()) throw new IllegalStateException("Etudiant non trouvé par son id");

        return student.get();
    }


/**
     * Retrieves the note of a student.
     *
     * @param id the ID of the student
     * @return the ResponseEntity containing the student's note
     */
    public ResponseEntity<?> getStudentNote(Long id) {

        Student upstudent = getcurrentstudent(id);

        return ResponseEntity.ok(upstudent.getNote());
    }

/**
     * Retrieves the information of a student.
     *
     * @param id the ID of the student
     * @return the ResponseEntity containing the student's information
     */
    public ResponseEntity<?> getStudentInfo(Long id) {
        Optional<User> user = userRepository.findById(id);
        return ResponseEntity.ok(studentRepository.findByuser(user.get()));
    }


}
