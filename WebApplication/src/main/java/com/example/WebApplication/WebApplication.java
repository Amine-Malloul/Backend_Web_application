package com.example.WebApplication;

import com.example.WebApplication.Model.ERole;
import com.example.WebApplication.Model.Maintenance;
import com.example.WebApplication.Model.Role;
import com.example.WebApplication.Model.User;
import com.example.WebApplication.Repository.MaintenanceRepository;
import com.example.WebApplication.Repository.RoleRepository;
import com.example.WebApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;

@SpringBootApplication
public class WebApplication implements CommandLineRunner {

	private final MaintenanceRepository maintenanceRepository;
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public WebApplication(MaintenanceRepository maintenanceRepository, RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.maintenanceRepository = maintenanceRepository;
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		Maintenance maintenanceonlyvalue = null;
		if (maintenanceRepository.count() == 0) {
			maintenanceonlyvalue = new Maintenance(true);

			maintenanceRepository.save(maintenanceonlyvalue);
		}

		Role admin_role = null;
		Role user_role = null;
		if (roleRepository.count() == 0){
			admin_role = new Role(ERole.ROLE_ADMIN);
			user_role = new Role(ERole.ROLE_USER);

			roleRepository.save(admin_role);
			roleRepository.save(user_role);
		}


		if (!userRepository.findByUsername("admin").isPresent()) {
			String encodedPassword = passwordEncoder.
					encode("adminm2i");

			User adminUser = new User("admin",
					"m2i.fso@gmai.com",
					encodedPassword,
					true,
					new HashSet<>(roleRepository.findAll()));

			userRepository.save(adminUser);
		}


	}
}
