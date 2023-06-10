package com.example.WebApplication.Service;


import com.example.WebApplication.Model.Maintenance;
import com.example.WebApplication.Model.Student;
import com.example.WebApplication.Repository.MaintenanceRepository;
import com.example.WebApplication.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final StudentRepository studentRepository;
    private final MaintenanceRepository maintenanceRepository;


    @Autowired
    public AdminService(StudentRepository studentRepository, MaintenanceRepository maintenanceRepository) {
        this.studentRepository = studentRepository;
        this.maintenanceRepository = maintenanceRepository;
    }


    // Method to sort students based on type and option
    public List<Student> sortstudents(String type, Integer option){
        List<Student> studentList = null;
        studentList = studentRepository.findAll();

        if (studentList.isEmpty()) {
            return null;
        } else {
            cleanStudents(studentList);
        }

        Comparator<Student> compareBynote = null;

        if (Objects.equals(type, "ASC")){
            compareBynote = Comparator.comparingDouble(Student::getNote);
        }else{
            compareBynote = Comparator.comparingDouble(Student::getNote).reversed();
        }


        Comparator<Student> compareByBranch = Comparator.comparing(Student::getLicencetype)
                .thenComparing(compareBynote);

        Comparator<Student> compareByUni = Comparator.comparing(Student::getUni)
                .thenComparing(compareBynote);

        Comparator<Student> compareByFac = Comparator.comparing(Student::getFac)
                .thenComparing(compareBynote);

        List<Student> sortedStudents = null;
        if (option == 1){
            sortedStudents = studentList.stream().sorted(compareByBranch).collect(Collectors.toList());
        }else if (option == 2){
            sortedStudents = studentList.stream().sorted(compareByUni).collect(Collectors.toList());
        }else{
            sortedStudents = studentList.stream().sorted(compareByFac).collect(Collectors.toList());
        }
        return sortedStudents;
    }



    // Helper method to remove invalid student entries
    private void cleanStudents(List<Student> studentList) {
        studentList.removeIf(student -> Objects.equals(student.getName(), "") || student.getName() == null);
    }


    // Method to get branch statistics
    public List<Double> getbranchs() {
        List<String> stringList = new ArrayList<>();
        List<Student> studentList = studentRepository.findAll();
        if (studentList.isEmpty())
            return null;

        cleanStudents(studentList);

        studentList.forEach(student -> {
            stringList.add(student.getLicencetype());
        });

        int a = 0, b = 0, c = 0;
        for (String branch : stringList) {
            if (Objects.equals(branch, "smi")) {
                a++;
            }else if (Objects.equals(branch, "diplome_etranger")) {
                b++;
            }else c++;
        }

        List<Double> list = new ArrayList<>();
        double n = (double) studentList.size();

        list.add(a / n);
        list.add(b / n);
        list.add(c / n);

        return list;

    }


	// Method to get isnormals statistics
    public List<Double> getisnormals() {
        List<Student> studentList = studentRepository.findAll();
        if (studentList.isEmpty())
            return null;

        cleanStudents(studentList);

        int n1 = 0, n2 = 0, n3 = 0, n4 = 0, n5 = 0, n6 = 0;
        for (Student student : studentList) {
            if (student.isIsnormals1()) n1++;

            if (student.isIsnormals2()) n2++;

            if (student.isIsnormals3()) n3++;

            if (student.isIsnormals4()) n4++;

            if (student.isIsnormals5()) n5++;

            if (student.isIsnormals6()) n6++;
        }

        double n = studentList.size();
        List<Double> list = new ArrayList<>();
        list.add(n1 / n);
        list.add(n2 / n);
        list.add(n3 / n);
        list.add(n4 / n);
        list.add(n5 / n);
        list.add(n6 / n);

        return list;

    }


    // Method to enable student application
    @Transactional
    public void enableApply() {
        Optional<Maintenance> maintenance1 = maintenanceRepository.findById(1L) ;
        Maintenance maintenance = null;
        if (maintenance1.isPresent())  maintenance = maintenance1.get();

        maintenance.setEnabled(true);

    }

    // Method to disable student application
    @Transactional
    public void disableApply() {
        Optional<Maintenance> maintenance1 = maintenanceRepository.findById(1L) ;
        Maintenance maintenance = null;
        if (maintenance1.isPresent())  maintenance = maintenance1.get();

        maintenance.setEnabled(false);

    }
}