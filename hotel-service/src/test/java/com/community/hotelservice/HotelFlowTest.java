package com.community.hotelservice;

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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false"
})
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
class HotelFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private BookingRepository bookingRepository;
    @MockBean private HotelRepository hotelRepository;
    @MockBean private ReviewRepository reviewRepository;

    @Test
    void fullFlowTest() throws Exception {

        // --- 1. MOCK SETUP ---
        Hotel mockHotel = new Hotel();
        mockHotel.setId(1L);
        mockHotel.setName("Flow Hotel");
        mockHotel.setLocation("Delhi");

        // Mock for CREATE
        when(hotelRepository.save(any(Hotel.class))).thenReturn(mockHotel);

        when(hotelRepository.save(argThat(hotel -> hotel.getName() != null && !hotel.getName().isEmpty())))
                .thenReturn(mockHotel);
        // Mock for GET ALL (Paginated)
        Page<Hotel> hotelPage = new PageImpl<>(List.of(mockHotel));
        when(hotelRepository.findAll(any(Pageable.class))).thenReturn(hotelPage);

        // Mock for DELETE (Service checks findById first)
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(mockHotel));

        // --- 2. EXECUTE STEPS ---

        // Step 1: CREATE (Expect 200)
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Flow Hotel"));

        // Step 2: GET ALL (Expect 200)
        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));

        // Step 3: INVALID DATA (Expect 400)
        // NOTE: Ensure your HotelDto class has @NotBlank on 'name'
        // and @Max(5) on 'rating' for this to pass.
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

        // Step 4: DELETE (Expect 200)
        mockMvc.perform(delete("/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted successfully"));
    }
}