package com.gpsolutions.propertyview;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpsolutions.propertyview.dto.AddressDto;
import com.gpsolutions.propertyview.dto.ContactsDto;
import com.gpsolutions.propertyview.dto.ArrivalTimeDto;
import com.gpsolutions.propertyview.dto.CreateHotelRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(statements = {
        "delete from hotel_amenities",
        "delete from hotels"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class HotelControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAndFetchHotel() throws Exception {
        AddressDto address = new AddressDto("9", "Pobediteley Avenue", "Minsk", "Belarus", "220004");
        ContactsDto contacts = new ContactsDto("+375 17 309-80-00", "doubletreeminsk.info@hilton.com");
        ArrivalTimeDto arrivalTime = new ArrivalTimeDto("14:00", "12:00");

        CreateHotelRequest request = new CreateHotelRequest(
                "DoubleTree by Hilton Minsk",
                "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...",
                "Hilton",
                address,
                contacts,
                arrivalTime
        );

        String payload = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("DoubleTree by Hilton Minsk"))
                .andExpect(jsonPath("$.address").value("9 Pobediteley Avenue, Minsk, 220004, Belarus"))
                .andExpect(jsonPath("$.phone").value("+375 17 309-80-00"));

        // Verify list and search endpoints
        mockMvc.perform(get("/property-view/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());

        mockMvc.perform(get("/property-view/search").param("city", "Minsk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("DoubleTree by Hilton Minsk"));
    }

    @Test
    void validationErrorOnInvalidPhone() throws Exception {
        AddressDto address = new AddressDto("9", "Pobediteley Avenue", "Minsk", "Belarus", "220004");
        ContactsDto contacts = new ContactsDto("INVALID_PHONE", "doubletreeminsk.info@hilton.com");
        ArrivalTimeDto arrivalTime = new ArrivalTimeDto("14:00", "12:00");

        CreateHotelRequest request = new CreateHotelRequest(
                "DoubleTree by Hilton Minsk",
                "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...",
                "Hilton",
                address,
                contacts,
                arrivalTime
        );

        String payload = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void histogramByCityAndAmenities() throws Exception {
        AddressDto address = new AddressDto("9", "Pobediteley Avenue", "Minsk", "Belarus", "220004");
        ContactsDto contacts = new ContactsDto("+375 17 309-80-00", "doubletreeminsk.info@hilton.com");
        ArrivalTimeDto arrivalTime = new ArrivalTimeDto("14:00", "12:00");

        CreateHotelRequest request = new CreateHotelRequest(
                "DoubleTree by Hilton Minsk",
                "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...",
                "Hilton",
                address,
                contacts,
                arrivalTime
        );

        String payload = objectMapper.writeValueAsString(request);

        String response = mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // add amenities
        mockMvc.perform(post("/property-view/hotels/{id}/amenities", extractId(response))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                ["Free parking", "Free WiFi"]
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amenities[0]").value("Free parking"));

        mockMvc.perform(get("/property-view/histogram/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Minsk").value(1));

        mockMvc.perform(get("/property-view/histogram/amenities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['Free WiFi']").value(1));
    }

    private String extractId(String responseJson) throws Exception {
        return objectMapper.readTree(responseJson).get("id").asText();
    }
}

