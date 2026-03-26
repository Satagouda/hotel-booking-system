package com.community.hotelservice.service;

import com.community.hotelservice.entity.Booking;
import com.community.hotelservice.repo.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository repository;

    public Booking book(Booking booking) {
        return repository.save(booking);
    }

    public void cancel(Long id) {
        repository.deleteById(id);
    }
}
