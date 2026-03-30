package com.community.hotelservice.controller;

import com.community.hotelservice.entity.Hotel;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private BookingRepository bookingRepository;
    @MockBean private HotelRepository hotelRepository;
    @MockBean private ReviewRepository reviewRepository;

    @Test
    void testCreateHotel() throws Exception {
        // --- 1. MOCK BEHAVIOR ---
        Hotel mockSavedHotel = new Hotel();
        mockSavedHotel.setId(1L);
        mockSavedHotel.setName("Test Hotel");
        mockSavedHotel.setLocation("Bangalore");

        // When repository.save is called, return our mock object instead of null
        when(hotelRepository.save(any(Hotel.class))).thenReturn(mockSavedHotel);

        String json = """
        {
          "name": "Test Hotel",
          "location": "Bangalore",
          "rating": 4,
          "price": 2000
        }
        """;

        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Hotel"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testPagination() throws Exception {
        // --- 2. MOCK PAGINATION ---
        Hotel mockHotel = new Hotel();
        mockHotel.setId(1L);
        mockHotel.setName("Test Hotel");

        // We MUST return a Page object, otherwise service.getAll() crashes on .isEmpty()
        Page<Hotel> hotelPage = new PageImpl<>(List.of(mockHotel));

        when(hotelRepository.findAll(any(Pageable.class))).thenReturn(hotelPage);

        mockMvc.perform(get("/hotels?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Test Hotel"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}