package com.community.hotelservice.service;

import com.community.hotelservice.entity.Hotel;
import com.community.hotelservice.repo.HotelRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {

    public HotelService(HotelRepository repository) {
        this.repository = repository;
    }

    // CACHE APPLIED HERE
    @Cacheable("hotels")
    public List<Hotel> getHotels() {
        return repository.findAll();
    }

    private final HotelRepository repository;

    public Hotel create(Hotel hotel) {
        return repository.save(hotel);
    }


    public List<Hotel> searchByLocation(String location) {
        return repository.findByLocation(location);
    }

    public Page<Hotel> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
    public void delete(Long id) {
        repository.deleteById(id);
    }
}