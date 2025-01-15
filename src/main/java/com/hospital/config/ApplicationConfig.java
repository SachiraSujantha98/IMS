package com.hospital.config;

import com.hospital.model.User;
import com.hospital.model.UserRole;
import com.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Autowired
    private UserService userService;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            // Create admin user if it doesn't exist
            if (userService.getUserByUsername("admin") == null) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("admin123");
                admin.setName("System Admin");
                admin.setRole(UserRole.ADMIN);
                userService.createUser(admin);
                System.out.println("Admin user created with username: admin and password: admin123");
            }
        };
    }
}