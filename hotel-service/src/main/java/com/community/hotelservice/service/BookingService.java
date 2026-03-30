package com.community.hotelservice.service;

import com.community.hotelservice.entity.Booking;
import com.community.hotelservice.exception.ResourceNotFoundException;
import com.community.hotelservice.repo.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.print.Book;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository repository;

    public Booking book(Booking booking) {
        return repository.save(booking);
    }

    public void cancel(Long id) {
        Booking booking = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id : "+ id));
        repository.delete(booking);
    }
}
