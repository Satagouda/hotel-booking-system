package com.community.hotelservice.controller;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
// 1. DISABLE the real database connection for this test
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 2. MOCK the repositories so Spring doesn't look for a real DB
    @MockBean private BookingRepository bookingRepository;
    @MockBean private HotelRepository hotelRepository; // Required by HotelService
    @MockBean private ReviewRepository reviewRepository; // Required by ReviewService
    // If you have a HotelRepository or RoomRepository, add @MockBean for them here too:
    // @MockBean
    // private HotelRepository hotelRepository;

    @Test
    void testCreateBooking() throws Exception {
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
        mockMvc.perform(delete("/bookings/" + id))
                .andExpect(status().isOk());
    }
}