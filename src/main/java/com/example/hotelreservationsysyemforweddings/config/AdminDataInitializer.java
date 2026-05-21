package com.example.hotelreservationsysyemforweddings.config;

import com.example.hotelreservationsysyemforweddings.model.Admin;
import com.example.hotelreservationsysyemforweddings.model.AdminRole;
import com.example.hotelreservationsysyemforweddings.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminDataInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if the IT Officer admin already exists
        if (adminRepository.findByEmail("it.officer@hotel.com").isEmpty()) {
            Admin itOfficer = new Admin();
            itOfficer.setName("IT Officer");
            itOfficer.setEmail("it.officer@hotel.com");
            itOfficer.setPassword(passwordEncoder.encode("password123")); // Use a secure default password
            itOfficer.setRole(AdminRole.ROLE_IT_OFFICER);
            adminRepository.save(itOfficer);
            System.out.println("Default IT Officer created. Email: it.officer@hotel.com, Password: password123");
        }
    }
}

