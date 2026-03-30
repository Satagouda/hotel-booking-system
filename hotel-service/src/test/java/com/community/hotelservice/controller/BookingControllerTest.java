package com.community.hotelservice.controller;

import com.community.hotelservice.entity.Booking;
import com.community.hotelservice.repo.BookingRepository;
import com.community.hotelservice.repo.HotelRepository;
import com.community.hotelservice.repo.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false"
})
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private BookingRepository bookingRepository;
    @MockBean private HotelRepository hotelRepository;
    @MockBean private ReviewRepository reviewRepository;

    @Test
    void testCreateBooking() throws Exception {
        // 1. MOCK THE SAVE: Return a booking with an ID
        Booking mockBooking = new Booking();
        mockBooking.setId(1L);
        mockBooking.setUserName("John");

        when(bookingRepository.save(any(Booking.class))).thenReturn(mockBooking);

        String json = """
        {
          "hotelId": 1,
          "userName": "John",
          "checkIn": "2026-04-01",
          "checkOut": "2026-04-05"
        }
        """;

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testCancelBooking() throws Exception {
        Long id = 1L;

        // 2. MOCK THE FIND: Service needs to find the booking before deleting it
        Booking existingBooking = new Booking();
        existingBooking.setId(id);

        when(bookingRepository.findById(id)).thenReturn(Optional.of(existingBooking));

        mockMvc.perform(delete("/bookings/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string("Cancelled"));
    }
}