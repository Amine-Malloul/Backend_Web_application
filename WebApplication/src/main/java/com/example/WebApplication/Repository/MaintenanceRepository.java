package com.example.WebApplication.Repository;

import com.example.WebApplication.Model.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

    // Find a Maintenance entity by its ID
    Optional<Maintenance> findById(Long id);
}
