package com.community.hotelservice.util;

import com.community.hotelservice.dto.HotelDto;
import com.community.hotelservice.entity.Hotel;

public class Mapper {

    public static HotelDto toDto(Hotel hotel){
        HotelDto dto = new HotelDto();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setLocation(hotel.getLocation());
        dto.setRating(hotel.getRating());
        dto.setPrice(hotel.getPrice());
        return dto;
    }

    public static Hotel toEntity(HotelDto dto) {
        Hotel hotel = new Hotel();
        hotel.setId(dto.getId());
        hotel.setName(dto.getName());
        hotel.setLocation(dto.getLocation());
        hotel.setRating(dto.getRating());
        hotel.setPrice(dto.getPrice());
        return hotel;
    }
}
