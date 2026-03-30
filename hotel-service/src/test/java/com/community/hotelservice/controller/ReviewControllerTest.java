package com.community.hotelservice.controller;

import com.community.hotelservice.entity.Review;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false"
})
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private BookingRepository bookingRepository;
    @MockBean private HotelRepository hotelRepository;
    @MockBean private ReviewRepository reviewRepository;

    @Test
    void testAddReview() throws Exception {
        // --- 1. MOCK THE SAVE ---
        Review mockReview = new Review();
        mockReview.setId(1L);
        mockReview.setHotelId(1L);
        mockReview.setComment("Great stay!");
        mockReview.setRating(5);

        when(reviewRepository.save(any(Review.class))).thenReturn(mockReview);

        String json = """
        {
          "hotelId": 1,
          "comment": "Great stay!",
          "rating": 5
        }
        """;

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.comment").value("Great stay!"));
    }

    @Test
    void testGetReviews() throws Exception {
        // --- 2. MOCK THE LIST ---
        Review mockReview = new Review();
        mockReview.setHotelId(1L);
        mockReview.setComment("Excellent service");

        // We MUST return a list with at least one item,
        // otherwise ReviewService will throw ResourceNotFoundException
        List<Review> mockList = List.of(mockReview);

        when(reviewRepository.findByHotelId(1L)).thenReturn(mockList);

        mockMvc.perform(get("/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comment").value("Excellent service"));
    }
}