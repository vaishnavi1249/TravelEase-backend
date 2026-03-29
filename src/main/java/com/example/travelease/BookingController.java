package com.example.travelease;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/my-bookings")
    public List<Booking> getMyBookings(Principal principal) {
        if (principal == null)
            return List.of();
        return bookingRepository.findByUserEmail(principal.getName());
    }

    @PostMapping
    public Booking createBooking(@RequestBody Booking booking, Principal principal) {
        if (principal != null) {
            booking.setUserEmail(principal.getName());
        } else {
            booking.setUserEmail("guest@example.com"); // Fallback safety
        }
        booking.setBookingDate(LocalDateTime.now().toString());
        booking.setStatus("CONFIRMED");
        return bookingRepository.save(booking);
    }
}
