package com.example.WebApplication.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Entity class representing maintenance settings.
 */

@Entity @Data @NoArgsConstructor
@Table(name = "maintenance")
public class Maintenance {

	/**
     * The unique identifier for maintenance.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maintenance_id;



    private boolean enabled;

	/**
     * Constructor for Maintenance class.
     *
     * @param enabled Flag indicating if maintenance is enabled or disabled.
     */

    public Maintenance(boolean enabled) {
        this.enabled = enabled;
    }
}
