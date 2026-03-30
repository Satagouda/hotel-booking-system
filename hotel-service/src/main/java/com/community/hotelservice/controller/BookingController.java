package com.community.hotelservice.controller;

import com.community.hotelservice.entity.Booking;
import com.community.hotelservice.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService service;

    @PostMapping
    public ResponseEntity<Booking> book(@Valid @RequestBody Booking booking) {
        return ResponseEntity.ok(service.book(booking));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        service.cancel(id);
        return ResponseEntity.ok("Cancelled");
    }
}