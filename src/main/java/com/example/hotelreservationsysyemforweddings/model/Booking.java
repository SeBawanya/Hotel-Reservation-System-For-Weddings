package com.example.hotelreservationsysyemforweddings.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate bookingDate;

    @ManyToOne
    @JoinColumn(name = "ballroom_id", nullable = false)
    private Ballroom ballroom;

    @ManyToOne
    @JoinColumn(name = "wedding_package_id", nullable = false)
    private WeddingPackage weddingPackage;

    private double totalPrice;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    // START OF NEW CODE
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "booking_vendors",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "vendor_id")
    )
    private Set<Vendor> vendors = new HashSet<>();
    // END OF NEW CODE

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
    public Ballroom getBallroom() { return ballroom; }
    public void setBallroom(Ballroom ballroom) { this.ballroom = ballroom; }
    public WeddingPackage getWeddingPackage() { return weddingPackage; }
    public void setWeddingPackage(WeddingPackage weddingPackage) { this.weddingPackage = weddingPackage; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    // START OF NEW GETTER/SETTER
    public Set<Vendor> getVendors() { return vendors; }
    public void setVendors(Set<Vendor> vendors) { this.vendors = vendors; }
    // END OF NEW GETTER/SETTER
}