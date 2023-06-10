package com.example.WebApplication.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data @AllArgsConstructor
public class StudentInforequest {

    private String name;
    private String lastname;
    private String cin;
    private String nationality;
    private LocalDate dob;
    private String address;
    private String phone;
    private String profession_state;
    private int bac_year;
    private String licencetype;
    private String uni;
    private String fac;
    private int first_year;
    private int licence_year;


    private double notes1;
    private boolean isnormals1;

    private double notes2;
    private boolean isnormals2;

    private double notes3;
    private boolean isnormals3;

    private double notes4;
    private boolean isnormals4;

    private double notes5;
    private boolean isnormals5;

    private double notes6;
    private boolean isnormals6;

}
