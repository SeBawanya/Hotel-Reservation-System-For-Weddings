// CREATE NEW FILE: src/main/java/com/example/hotelreservationsysyemforweddings/model/dto/BookingRequestDTO.java
package com.example.hotelreservationsysyemforweddings.model.dto;

import java.time.LocalDate;
import java.util.List;

public class BookingRequestDTO {
    private LocalDate bookingDate;
    private Long ballroomId;
    private Long weddingPackageId;
    private double totalPrice;
    private List<Long> vendorIds;

    // Getters and Setters
    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
    public Long getBallroomId() { return ballroomId; }
    public void setBallroomId(Long ballroomId) { this.ballroomId = ballroomId; }
    public Long getWeddingPackageId() { return weddingPackageId; }
    public void setWeddingPackageId(Long weddingPackageId) { this.weddingPackageId = weddingPackageId; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public List<Long> getVendorIds() { return vendorIds; }
    public void setVendorIds(List<Long> vendorIds) { this.vendorIds = vendorIds; }
}