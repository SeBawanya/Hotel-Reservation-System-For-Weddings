package com.example.hotelreservationsysyemforweddings.controller;

import com.example.hotelreservationsysyemforweddings.model.Customer;
import com.example.hotelreservationsysyemforweddings.model.dto.CustomerAdminViewDTO; // Import the new DTO
import com.example.hotelreservationsysyemforweddings.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors; // Import Collectors

@RestController
@RequestMapping("/api/manage/customers")
@PreAuthorize("hasAnyRole('HOTEL_MANAGER', 'IT_OFFICER')")
public class ManageCustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    // START OF MODIFICATION
    // This method now returns a List of DTOs instead of the raw Customer entity.
    // This is a robust way to prevent any serialization errors.
    @GetMapping
    public List<CustomerAdminViewDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customer -> {
                    CustomerAdminViewDTO dto = new CustomerAdminViewDTO();
                    dto.setId(customer.getId());
                    dto.setName(customer.getName());
                    dto.setEmail(customer.getEmail());
                    dto.setPhone(customer.getPhone());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    // END OF MODIFICATION

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Customer> existingCustomerWithEmail = customerRepository.findByEmail(customerDetails.getEmail());
        if (existingCustomerWithEmail.isPresent() && !existingCustomerWithEmail.get().getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Email is already in use by another customer."));
        }

        Customer existingCustomer = optionalCustomer.get();
        existingCustomer.setName(customerDetails.getName());
        existingCustomer.setEmail(customerDetails.getEmail());
        existingCustomer.setPhone(customerDetails.getPhone());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        if (!customerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        customerRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}