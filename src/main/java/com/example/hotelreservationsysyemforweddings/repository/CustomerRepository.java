package com.example.hotelreservationsysyemforweddings.repository;

import com.example.hotelreservationsysyemforweddings.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Method to find a customer by their email address
    Optional<Customer> findByEmail(String email);
}
