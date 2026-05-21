// CREATE NEW FILE: src/main/java/com/example/hotelreservationsysyemforweddings/model/dto/BookingResponseDTO.java
package com.example.hotelreservationsysyemforweddings.model.dto;

import com.example.hotelreservationsysyemforweddings.model.Ballroom;
import com.example.hotelreservationsysyemforweddings.model.WeddingPackage;
import java.time.LocalDate;

public class BookingResponseDTO {
    private Long id;
    private LocalDate bookingDate;
    private double totalPrice;
    private Ballroom ballroom;
    private WeddingPackage weddingPackage;
    private CustomerDTO customer;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public Ballroom getBallroom() { return ballroom; }
    public void setBallroom(Ballroom ballroom) { this.ballroom = ballroom; }
    public WeddingPackage getWeddingPackage() { return weddingPackage; }
    public void setWeddingPackage(WeddingPackage weddingPackage) { this.weddingPackage = weddingPackage; }
    public CustomerDTO getCustomer() { return customer; }
    public void setCustomer(CustomerDTO customer) { this.customer = customer; }
}