package com.example.hotelreservationsysyemforweddings.controller;

import com.example.hotelreservationsysyemforweddings.model.Customer;
import com.example.hotelreservationsysyemforweddings.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * Gets the profile information for the currently authenticated user.
     * @param principal The security principal representing the logged-in user.
     * @return The customer's details or a 404 if not found.
     */
    @GetMapping("/me")
    public ResponseEntity<Customer> getCurrentCustomer(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        return customerService.findByEmail(principal.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates the name and phone number for the currently authenticated user.
     * @param payload A map containing the new "name" and "phone".
     * @param principal The security principal representing the logged-in user.
     * @return A success or error message.
     */
    @PutMapping("/me/update")
    public ResponseEntity<?> updateCustomerDetails(@RequestBody Map<String, String> payload, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build(); // Unauthorized
        try {
            customerService.updateCustomerDetails(principal.getName(), payload.get("name"), payload.get("phone"));
            return ResponseEntity.ok(Map.of("message", "Profile updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Could not update profile."));
        }
    }

    /**
     * Changes the password for the currently authenticated user.
     * @param payload A map containing "currentPassword" and "newPassword".
     * @param principal The security principal representing the logged-in user.
     * @return A success or error message.
     */
    @PostMapping("/me/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> payload, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build(); // Unauthorized
        try {
            customerService.changePassword(
                    principal.getName(),
                    payload.get("currentPassword"),
                    payload.get("newPassword")
            );
            return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "An error occurred while changing the password."));
        }
    }

    /**
     * Deletes the account of the currently authenticated user.
     * @param principal The security principal representing the logged-in user.
     * @param request The HTTP request to invalidate the session.
     * @return A success or error message.
     */
    @DeleteMapping("/me/delete")
    public ResponseEntity<?> deleteAccount(Principal principal, HttpServletRequest request) {
        if (principal == null) return ResponseEntity.status(401).build(); // Unauthorized
        try {
            customerService.deleteAccount(principal.getName());
            // Invalidate the session to log the user out
            SecurityContextHolder.clearContext();
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            return ResponseEntity.ok(Map.of("message", "Account deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Could not delete account."));
        }
    }

    /**
     * Legacy endpoint for updating only the phone number from the booking page.
     * @deprecated Use the PUT /me/update endpoint instead.
     */
    @PostMapping("/update-phone")
    public ResponseEntity<?> updatePhoneNumber(@RequestBody Map<String, String> payload, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).build(); // Unauthorized
        try {
            customerService.updateCustomerPhone(principal.getName(), payload.get("phone"));
            return ResponseEntity.ok(Map.of("message", "Phone number updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Could not update phone number."));
        }
    }
}

