package com.example.hotelreservationsysyemforweddings.service;

import com.example.hotelreservationsysyemforweddings.model.*;
import com.example.hotelreservationsysyemforweddings.model.dto.BookingRequestDTO;
import com.example.hotelreservationsysyemforweddings.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
public class BookingService {

    @Autowired private BookingRepository bookingRepository;
    @Autowired private BallroomRepository ballroomRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private WeddingPackageRepository weddingPackageRepository;
    @Autowired private VendorRepository vendorRepository; // Added

    public boolean isBallroomAvailable(Long ballroomId, LocalDate date) {
        return bookingRepository.findByBallroomIdAndBookingDate(ballroomId, date).isEmpty();
    }

    public List<Ballroom> getAllBallrooms() {
        return ballroomRepository.findAll();
    }

    // START OF MODIFICATION
    @Transactional
    public Booking createBooking(BookingRequestDTO bookingRequest, String customerEmail) {
        Customer customer = customerRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found. Please log in again."));

        Ballroom ballroom = ballroomRepository.findById(bookingRequest.getBallroomId())
                .orElseThrow(() -> new NoSuchElementException("Ballroom not found with id: " + bookingRequest.getBallroomId()));

        WeddingPackage weddingPackage = weddingPackageRepository.findById(bookingRequest.getWeddingPackageId())
                .orElseThrow(() -> new NoSuchElementException("Wedding Package not found with id: " + bookingRequest.getWeddingPackageId()));

        Set<Vendor> selectedVendors = new HashSet<>(vendorRepository.findAllById(bookingRequest.getVendorIds()));

        Booking newBooking = new Booking();
        newBooking.setCustomer(customer);
        newBooking.setBallroom(ballroom);
        newBooking.setWeddingPackage(weddingPackage);
        newBooking.setBookingDate(bookingRequest.getBookingDate());
        newBooking.setTotalPrice(bookingRequest.getTotalPrice());
        newBooking.setVendors(selectedVendors);

        return bookingRepository.save(newBooking);
    }
    // END OF MODIFICATION

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    public List<Booking> findBookingsByCustomer(Customer customer) {
        return bookingRepository.findAllByCustomer(customer);
    }

    @Transactional
    public Optional<Booking> updateBooking(Long bookingId, LocalDate newDate, Long newBallroomId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            return Optional.empty();
        }

        Optional<Booking> conflictingBooking = bookingRepository.findByBallroomIdAndBookingDate(newBallroomId, newDate);
        if (conflictingBooking.isPresent() && !conflictingBooking.get().getId().equals(bookingId)) {
            throw new IllegalStateException("The selected date and ballroom is already booked by another party.");
        }

        Ballroom newBallroom = ballroomRepository.findById(newBallroomId)
                .orElseThrow(() -> new NoSuchElementException("Ballroom not found with id: " + newBallroomId));

        Booking bookingToUpdate = optionalBooking.get();
        bookingToUpdate.setBookingDate(newDate);
        bookingToUpdate.setBallroom(newBallroom);

        return Optional.of(bookingRepository.save(bookingToUpdate));
    }
}