package com.example.hotelreservationsysyemforweddings.model.dto;

// This is a Data Transfer Object (DTO) used to safely send customer
// data to the admin frontend. It only includes the fields needed for the view.
public class CustomerAdminViewDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}