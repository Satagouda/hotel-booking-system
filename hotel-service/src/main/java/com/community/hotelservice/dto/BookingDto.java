package com.community.hotelservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingDto {
    private Long id;
    private Long hotelId;
    private String userName;
    private LocalDate checkIn;
    private LocalDate checkOut;
}