// CREATE NEW FILE: src/main/java/com/example/hotelreservationsysyemforweddings/model/VendorCategory.java
package com.example.hotelreservationsysyemforweddings.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "vendor_categories")
public class VendorCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Vendor> vendors;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Vendor> getVendors() { return vendors; }
    public void setVendors(List<Vendor> vendors) { this.vendors = vendors; }
}