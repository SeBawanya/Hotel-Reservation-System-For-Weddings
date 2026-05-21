package com.example.hotelreservationsysyemforweddings.service;

import com.example.hotelreservationsysyemforweddings.model.Customer;
import com.example.hotelreservationsysyemforweddings.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registers a new customer by encoding their password before saving.
     * @param customer The new customer object.
     * @return The saved customer with an encoded password.
     */
    public Customer registerCustomer(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }

    /**
     * Finds a customer by their email address.
     * @param email The email to search for.
     * @return An Optional containing the customer if found.
     */
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    /**
     * Updates the name and phone number of an existing customer.
     * @param email The email of the customer to update.
     * @param name The new full name.
     * @param phone The new phone number.
     */
    @Transactional
    public void updateCustomerDetails(String email, String name, String phone) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with email: " + email));
        customer.setName(name);
        customer.setPhone(phone);
        customerRepository.save(customer);
    }

    /**
     * Changes the password for a given user after verifying their current password.
     * @param email The email of the customer.
     * @param currentPassword The user's current password.
     * @param newPassword The desired new password.
     */
    @Transactional
    public void changePassword(String email, String currentPassword, String newPassword) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Verify that the provided current password matches the stored one
        if (!passwordEncoder.matches(currentPassword, customer.getPassword())) {
            throw new IllegalArgumentException("Incorrect current password");
        }

        // Basic validation for the new password
        if (newPassword == null || newPassword.length() < 4) {
            throw new IllegalArgumentException("New password must be at least 4 characters long");
        }

        // Encode and save the new password
        customer.setPassword(passwordEncoder.encode(newPassword));
        customerRepository.save(customer);
    }

    /**
     * Deletes a customer's account from the database.
     * @param email The email of the account to delete.
     */
    @Transactional
    public void deleteAccount(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        customerRepository.delete(customer);
    }

    /**
     * Updates only the phone number for a customer.
     * @param email The email of the customer to update.
     * @param phone The new phone number.
     */
    @Transactional
    public void updateCustomerPhone(String email, String phone) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with email: " + email));
        customer.setPhone(phone);
        customerRepository.save(customer);
    }
}

