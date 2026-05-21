package com.example.hotelreservationsysyemforweddings.controller;

import com.example.hotelreservationsysyemforweddings.model.Customer;
import com.example.hotelreservationsysyemforweddings.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody Customer customer) {
        if (customerService.findByEmail(customer.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Email already exists"));
        }
        Customer savedCustomer = customerService.registerCustomer(customer);
        return ResponseEntity.ok(Map.of("message", "Customer registered successfully", "customerId", savedCustomer.getId()));
    }

    /**
     * Endpoint to check the current authentication status and get user details.
     * This is called by the frontend to determine if a user is logged in.
     */
    @GetMapping("/status")
    public ResponseEntity<?> getAuthStatus(Principal principal) {
        if (principal != null) {
            Optional<Customer> customerOpt = customerService.findByEmail(principal.getName());
            if (customerOpt.isPresent()) {
                Customer customer = customerOpt.get();
                // START OF FIX: Handle the case where the phone number is null
                // The Map.of() factory method does not allow null values.
                // We check if the phone is null and provide an empty string as a default if it is.
                return ResponseEntity.ok(Map.of(
                        "isAuthenticated", true,
                        "name", customer.getName(),
                        "email", customer.getEmail(),
                        "phone", customer.getPhone() != null ? customer.getPhone() : ""
                ));
                // END OF FIX
            }
        }
        // If there is no principal, the user is not authenticated.
        return ResponseEntity.ok(Map.of("isAuthenticated", false));
    }
}
