package com.example.hotelreservationsysyemforweddings.controller;

import com.example.hotelreservationsysyemforweddings.model.Ballroom;
import com.example.hotelreservationsysyemforweddings.model.Booking;
import com.example.hotelreservationsysyemforweddings.model.Customer;
import com.example.hotelreservationsysyemforweddings.model.dto.BookingRequestDTO;
import com.example.hotelreservationsysyemforweddings.model.dto.BookingResponseDTO;
import com.example.hotelreservationsysyemforweddings.model.dto.CustomerDTO;
import com.example.hotelreservationsysyemforweddings.repository.CustomerRepository;
import com.example.hotelreservationsysyemforweddings.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired private BookingService bookingService;
    @Autowired private CustomerRepository customerRepository;

    @GetMapping("/ballrooms")
    public List<Ballroom> getBallrooms() {
        return bookingService.getAllBallrooms();
    }

    @PostMapping("/check-availability")
    public ResponseEntity<Map<String, Boolean>> checkAvailability(@RequestBody Map<String, String> payload) {
        Long ballroomId = Long.parseLong(payload.get("ballroomId"));
        LocalDate date = LocalDate.parse(payload.get("date"));
        boolean isAvailable = bookingService.isBallroomAvailable(ballroomId, date);
        return ResponseEntity.ok(Map.of("isAvailable", isAvailable));
    }

    // START OF MODIFICATION
    @PostMapping
    public Booking createBooking(@RequestBody BookingRequestDTO bookingRequest, Principal principal) {
        return bookingService.createBooking(bookingRequest, principal.getName());
    }
    // END OF MODIFICATION

    @GetMapping("/my-bookings")
    public ResponseEntity<List<Booking>> getMyBookings(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        Customer customer = customerRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with email: " + principal.getName()));
        List<Booking> bookings = bookingService.findBookingsByCustomer(customer);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping
    public List<BookingResponseDTO> getAllBookings() {
        return bookingService.getAllBookings().stream().map(booking -> {
            BookingResponseDTO dto = new BookingResponseDTO();
            dto.setId(booking.getId());
            dto.setBookingDate(booking.getBookingDate());
            dto.setTotalPrice(booking.getTotalPrice());
            dto.setBallroom(booking.getBallroom());
            dto.setWeddingPackage(booking.getWeddingPackage());

            Customer customer = booking.getCustomer();
            if (customer != null) {
                dto.setCustomer(new CustomerDTO(customer.getName(), customer.getEmail(), customer.getPhone()));
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        try {
            LocalDate newDate = LocalDate.parse(payload.get("bookingDate"));
            Long newBallroomId = Long.parseLong(payload.get("ballroomId"));

            Optional<Booking> updatedBooking = bookingService.updateBooking(id, newDate, newBallroomId);

            return updatedBooking
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "An unexpected error occurred."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok().build();
    }
}