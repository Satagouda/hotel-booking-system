package com.community.hotelservice.service;

import com.community.hotelservice.entity.Hotel;
import com.community.hotelservice.exception.ResourceNotFoundException;
import com.community.hotelservice.repo.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "hotels") // central config
public class HotelService {

    private final HotelRepository repository;

    // WRITE → must clear cache
    @CacheEvict(allEntries = true)
    public Hotel create(Hotel hotel) {
        return repository.save(hotel);
    }

    //  CACHE SEARCH BY LOCATION
    @Cacheable(key = "'location:' + #location")
    public List<Hotel> searchByLocation(String location) {
        List<Hotel> hotels = repository.findByLocation(location);

        if (hotels.isEmpty()) {
            throw new ResourceNotFoundException("No hotels found in location: " + location);
        }

        return hotels;
    }

    //  CACHE ALL HOTELS (Pagination-based)
    @Cacheable(key = "'page:' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public Page<Hotel> getAll(Pageable pageable) {
        Page<Hotel> hotels = repository.findAll(pageable);

        if (hotels.isEmpty()) {
            throw new ResourceNotFoundException("No hotels available");
        }

        return hotels;
    }

    //  DELETE → must clear cache
    @CacheEvict(allEntries = true)
    public void delete(Long id) {
        Hotel hotel = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));

        repository.delete(hotel);
    }
}