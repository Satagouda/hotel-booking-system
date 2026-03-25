package com.community.hotelservice.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class HotelService {

    // CACHE APPLIED HERE
    @Cacheable("hotels")
    public String getHotels() {
        System.out.println("Fetching from DB (simulated)");
        return "List of hotels from DB";
    }
}