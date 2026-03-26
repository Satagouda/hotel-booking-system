package com.community.hotelservice;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
class HotelFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingRepository bookingRepository;
    @MockBean private HotelRepository hotelRepository; // Required by HotelService
    @MockBean private ReviewRepository reviewRepository; // Required by ReviewService


    @Test
    void fullFlowTest() throws Exception {

        // 1. CREATE
        String createJson = """
        {
          "name": "Flow Hotel",
          "location": "Delhi",
          "rating": 4,
          "price": 3000
        }
        """;

        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isOk());

        // 2. GET
        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk());

        // 3. INVALID UPDATE → expect 400
        String invalidJson = """
        {
          "name": "",
          "location": "Delhi",
          "rating": 10,
          "price": 3000
        }
        """;

        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        // 4. DELETE (assuming id = 1 for simplicity)
        mockMvc.perform(delete("/hotels/1"))
                .andExpect(status().isOk());
    }
}