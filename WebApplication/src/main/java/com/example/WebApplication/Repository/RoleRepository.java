package com.example.WebApplication.Repository;


import com.example.WebApplication.Model.ERole;
import com.example.WebApplication.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
    // Find a Role entity by its name
    Optional<Role> findByName(ERole name);

}
