package com.example.WebApplication.config;

import com.example.WebApplication.Model.Student;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class StudentCSVExporter {

    private List<Student> studentList = null;

    public StudentCSVExporter(List<Student> studentList) {
        this.studentList = studentList;
    }

    public void export(HttpServletResponse response) throws IOException {

        // Create a CsvBeanWriter instance to write CSV data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        
        // Define the CSV header and field name mapping
        String[] csvHeader = {"Nom", "Prénom", "CIN", "Note Finale"};
        String[] nameMapping = {"lastname", "name", "cin", "note"};

        // Write the CSV header
        csvWriter.writeHeader(csvHeader);

        // Write each student record to the CSV file
        for (Student student : studentList) {
            csvWriter.write(student, nameMapping);
        }

        // Close the CsvBeanWriter
        csvWriter.close();


    }
}
