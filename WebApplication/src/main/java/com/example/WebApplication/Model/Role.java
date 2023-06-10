package com.example.WebApplication.Model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

  /**
     * The unique identifier for the role.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long role_id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    private ERole name;


  /**
     * Constructor for the Role class.
     *
     * @param name The name of the role.
     */

    public Role(ERole name) {
        this.name = name;
    }


/**
     * Default constructor required by JPA.
     */

    public Role() {

    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }
}
