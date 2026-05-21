package com.example.hotelreservationsysyemforweddings.repository;

import com.example.hotelreservationsysyemforweddings.model.Booking;
import com.example.hotelreservationsysyemforweddings.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Custom query to find a booking by ballroom and date
    Optional<Booking> findByBallroomIdAndBookingDate(Long ballroomId, LocalDate bookingDate);

    // New method to find all bookings for a specific customer
    List<Booking> findAllByCustomer(Customer customer);
}

