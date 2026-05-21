package com.example.hotelreservationsysyemforweddings.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/status")
    public ResponseEntity<?> getStatus(Principal principal, Authentication authentication) {
        if (principal != null) {
            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse(null);

            // Determine user type based on role. Admins have roles like "ROLE_IT_OFFICER"
            String userType = (role != null && role.startsWith("ROLE_")) ? "ADMIN" : "CUSTOMER";

            return ResponseEntity.ok(Map.of(
                    "isAuthenticated", true,
                    "username", principal.getName(),
                    // In a real app you might want to fetch the full name from the respective repository
                    "name", principal.getName(),
                    "userType", userType,
                    "role", role != null ? role : ""
            ));
        }
        return ResponseEntity.ok(Map.of("isAuthenticated", false));
    }
}

