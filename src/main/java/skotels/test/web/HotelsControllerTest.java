package skotels.test.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import skotels.hotelapp.HotelappApplication;
import skotels.hotelapp.model.Hotels;
import skotels.hotelapp.service.HotelsService;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = HotelappApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class HotelsControllerTest {

    MockMvc mockMvc;

    @Autowired
    private HotelsService hotelsService;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldGetAllHotelsSuccessfully() throws Exception {
        mockMvc.perform(get("/api/hotels/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldFindAllByNameSuccessfully() throws Exception {
        List<Hotels> hotelsFromService = hotelsService.findHotelsByName("Hotel");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/hotels/searchHotels")
                .param("search", "Hotel");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<Hotels> hotelsFromResult = Arrays
                .asList(objectMapper.readValue(result.getResponse().getContentAsString(), Hotels[].class));

        for (int i = 0; i<hotelsFromResult.size(); ++i) {
            Assertions.assertEquals(hotelsFromService.get(i).getName(), hotelsFromResult.get(i).getName());
        }
    }

    @Test
    @WithMockUser(username = "username", roles={"ADMIN"})
    public void shouldSaveHotelSuccessfully() {
        String property = "property";
        Hotels newHotel = new Hotels(property, property, property, property, property, property, property, property,
                property, property, property);

        ResponseEntity<Hotels> result =
                restTemplate.postForEntity("/api/hotels/save", newHotel, Hotels.class);

        Assertions.assertTrue(result.hasBody());
        Assertions.assertEquals(newHotel.getName(), result.getBody().getName());
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    @WithMockUser(username = "username", roles={"ADMIN"})
    public void shouldDeleteHotelSuccessfully() {
        String property = "property";
        Hotels hotel = new Hotels(property, property, property, property, property, property, property, property,
                property, property, property);

        ResponseEntity<String> result =
                restTemplate.postForEntity("/api/hotels/delete", hotel, String.class);

        Assertions.assertTrue(result.hasBody());
        Assertions.assertEquals("Successfully deleted hotel", result.getBody());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
