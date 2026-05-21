package com.example.hotelreservationsysyemforweddings.controller;

import com.example.hotelreservationsysyemforweddings.model.Admin;
import com.example.hotelreservationsysyemforweddings.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication; // Import Authentication
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @PreAuthorize("hasRole('IT_OFFICER')")
    public ResponseEntity<?> createAdmin(@RequestBody Admin admin) {
        if (adminRepository.findByEmail(admin.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Email already exists"));
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        Admin savedAdmin = adminRepository.save(admin);
        return ResponseEntity.ok(savedAdmin);
    }

    @GetMapping
    @PreAuthorize("hasRole('IT_OFFICER')")
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('IT_OFFICER')")
    public ResponseEntity<?> updateAdmin(@PathVariable Long id, @RequestBody Admin adminDetails) {
        Optional<Admin> optionalAdmin = adminRepository.findById(id);
        if (optionalAdmin.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Admin> existingAdminWithEmail = adminRepository.findByEmail(adminDetails.getEmail());
        if (existingAdminWithEmail.isPresent() && !existingAdminWithEmail.get().getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Email already in use by another account."));
        }

        Admin existingAdmin = optionalAdmin.get();
        existingAdmin.setName(adminDetails.getName());
        existingAdmin.setEmail(adminDetails.getEmail());
        existingAdmin.setRole(adminDetails.getRole());

        Admin updatedAdmin = adminRepository.save(existingAdmin);
        return ResponseEntity.ok(updatedAdmin);
    }

    /**
     * START OF NEW METHOD
     * Endpoint to delete an admin user.
     * Accessible only by ROLE_IT_OFFICER.
     * Includes a security check to prevent a user from deleting their own account.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('IT_OFFICER')")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id, Authentication authentication) {
        // Find the admin to be deleted
        Optional<Admin> adminToDelete = adminRepository.findById(id);
        if (adminToDelete.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Security Check: Prevent self-deletion
        String loggedInUserEmail = authentication.getName();
        if (adminToDelete.get().getEmail().equals(loggedInUserEmail)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "You cannot delete your own account."));
        }

        adminRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
    // END OF NEW METHOD
}