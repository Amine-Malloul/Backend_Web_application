package com.example.WebApplication.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDate;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "Students", uniqueConstraints = {@UniqueConstraint(columnNames = "cin"),
        @UniqueConstraint(columnNames = "phone")})
public class Student implements Comparable<Student>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long student_id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Student(User user) {
        this.user = user;
    }

    //    private int final_note;

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


    private String licence_photo;

    private String personal_photo;


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

    @Transient
    private double note;

    @Override
    public int compareTo(@NotNull Student o) {
        return Double.compare(this.getNote(), o.getNote());
    }


/**
     * Calculates the total score (note) of the student based on their academic performance.
     *
     * @return The total score of the student.
     */

    public double getNote() {
        double nmoy_DEUG=0, moy_DEUG = (notes1+notes2+notes3+notes4) / 4;
        nmoy_DEUG = calculpts(nmoy_DEUG, moy_DEUG);

        double ptsSessionS1=0, ptsSessionS2=0, ptsSessionS3=0,
                ptsSessionS4=0, ptsSessionS5=0, ptsSessionS6=0;

        if ( isnormals1 ) ptsSessionS1+=0.5;
        if ( isnormals2 ) ptsSessionS2+=0.5;
        if ( isnormals3 ) ptsSessionS3+=0.5;
        if ( isnormals4 ) ptsSessionS4+=0.5;
        double pts_SessionDEUG = ptsSessionS1 + ptsSessionS2
                + ptsSessionS3 + ptsSessionS4;

        if ( isnormals5 ) ptsSessionS5+=1;
        if ( isnormals6 ) ptsSessionS6+=1;
        double pts_SessionLicence = ptsSessionS5 + ptsSessionS6;

        double nMoy_Licence=0, Moy_Licence = (notes5+notes6) / 2;
        nMoy_Licence = calculpts(nMoy_Licence, Moy_Licence);


        double ptsNombreAnUniv=0, NombreAnUniv = licence_year - first_year;
        if (NombreAnUniv == 3 ) ptsNombreAnUniv+=6;
        else if (NombreAnUniv == 4 ) ptsNombreAnUniv+=3;

        return nmoy_DEUG + pts_SessionDEUG + nMoy_Licence + pts_SessionLicence + ptsNombreAnUniv ;
    }

    private double calculpts(double y, double x) {
        if ( x >= 11 && x < 12 ) y+=2;
        else if ( x >= 12 && x < 13 ) y+=4;
        else if ( x >= 13 && x < 14 ) y+=6;
        else if (x >= 14 ) y+=8;
        return y;
    }

    @Transient
    public String getPhotosImagePath(){
        if ( personal_photo == null || student_id == null ) return null;

        return "/photos/student_photos/" + student_id + "/" + personal_photo;
    }



//    public void setNote(int note) {
//        this.note = note;
//    }


}
