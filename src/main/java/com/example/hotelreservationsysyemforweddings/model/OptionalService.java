package com.example.hotelreservationsysyemforweddings.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "optional_services")
public class OptionalService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wedding_package_id")
    @JsonBackReference
    private WeddingPackage weddingPackage;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public WeddingPackage getWeddingPackage() {
        return weddingPackage;
    }

    public void setWeddingPackage(WeddingPackage weddingPackage) {
        this.weddingPackage = weddingPackage;
    }
}
