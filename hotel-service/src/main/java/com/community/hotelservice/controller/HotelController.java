package com.community.hotelservice.controller;

import com.community.hotelservice.service.HotelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public String getHotels() {
        return hotelService.getHotels();
    }

    @GetMapping("/details")
    public String getHotelDetails(){
        return "Hotel details with reviews and images";
    }
}
