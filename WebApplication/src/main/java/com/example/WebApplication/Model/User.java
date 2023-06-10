package com.example.WebApplication.Model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "username")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @NotBlank
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String verificationCode;

    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    // Constructors

    /**
     * Default constructor
     */
    public User() {
    }

    /**
     * Constructor with parameters
     *
     * @param username        The username of the user
     * @param email           The email address of the user
     * @param password        The password of the user
     * @param verificationCode The verification code of the user
     * @param enabled         The flag indicating if the user is enabled
     */
    public User(String username, String email, String password, String verificationCode, boolean enabled) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.verificationCode = verificationCode;
        this.enabled = enabled;
    }

    /**
     * Constructor with username
     *
     * @param username The username of the user
     */
    public User(String username) {
        this.username = username;
    }

    /**
     * Constructor with user ID
     *
     * @param id The ID of the user
     */
    public User(Long id) {
        this.user_id = id;
    }

    /**
     * Constructor with parameters
     *
     * @param username The username of the user
     * @param email    The email address of the user
     * @param password The password of the user
     * @param enabled  The flag indicating if the user is enabled
     * @param roles    The roles assigned to the user
     */
    public User(String username, String email, String password, boolean enabled, Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
    }
}
