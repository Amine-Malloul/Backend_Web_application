package com.example.WebApplication.Controller;


import com.example.WebApplication.Model.Student;
import com.example.WebApplication.Service.AdminService;
import com.example.WebApplication.config.StudentCSVExporter;
import com.example.WebApplication.config.StudentPDFExpoter;
import com.example.WebApplication.payload.request.SelectionRequest;
import com.example.WebApplication.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

// Endpoint for getting all students in ascending order based on a specified attribute
    @GetMapping(path = "/sort/asc")
    public ResponseEntity<?> getAllStudents(@RequestParam String type,@RequestParam Integer option){

        List<Student> sortedStudents = adminService.sortstudents(type, option);
        if (sortedStudents == null) return ResponseEntity.badRequest()
                .body(new MessageResponse("aucun étudiant trouvé"));

        return ResponseEntity.ok(sortedStudents);

    }


// Endpoint for getting branch values
    @GetMapping(path = "/branch")
    public ResponseEntity<?> getbranchs(){

        List<Double> list =  adminService.getbranchs();
        if ( list.isEmpty() )
            return ResponseEntity.badRequest().body(new MessageResponse("aucun étudiant trouvé"));

        return ResponseEntity.ok(list);

    }

// Endpoint for getting session values
    @GetMapping(path = "/sessions")
    public ResponseEntity<?> getisnormals(){

        List<Double> list = adminService.getisnormals();

        if (list.isEmpty() || list == null)
            return ResponseEntity.badRequest().body(new MessageResponse("aucun étudiant trouvé"));

        return ResponseEntity.ok(list);

    }


// Endpoint for exporting student data to CSV or PDF format
    @GetMapping(path = "/sessions/export{mode}")
    public void exportToCSV(HttpServletResponse response, @RequestParam("type") String type,
                            @RequestParam("option") Integer option,
                            @PathVariable("mode") String mode) throws IOException {

        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        String headerValue = "attachement; filename=Students-" + currentDateTime +"." + mode;

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, headerValue);

        List<Student> studentList = adminService.sortstudents(type, option);

        if (Objects.equals(mode, "csv")) {
            response.setContentType("text/csv");
            StudentCSVExporter exporter = new StudentCSVExporter(studentList);
            exporter.export(response);
        }else {
            response.setContentType("application/pdf");
            StudentPDFExpoter exporter = new StudentPDFExpoter(studentList);
            exporter.export(response);
        }

    }


// Endpoint for enabling or disabling the Apply mode
    @PutMapping("/Apply/{mode}")
    public ResponseEntity<?> enable_disable_Apply(@PathVariable("mode") String mode ){
        if (Objects.equals(mode, "disable")) {
            adminService.disableApply();
            return ResponseEntity.ok(new MessageResponse("La candidature est désactivée !"));
        }

        adminService.enableApply();
        return ResponseEntity.ok("La candidature est activée !");
    }


}
