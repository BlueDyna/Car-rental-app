package be.ucll.se.aaron_abbey_backend.integration.http;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import be.ucll.se.aaron_abbey_backend.DTO.RentRequest;
import be.ucll.se.aaron_abbey_backend.authentication.JWT.JWTservice;
import be.ucll.se.aaron_abbey_backend.controller.RentController;
import be.ucll.se.aaron_abbey_backend.model.Car;
import be.ucll.se.aaron_abbey_backend.model.Rent;
import be.ucll.se.aaron_abbey_backend.model.Rental;
import be.ucll.se.aaron_abbey_backend.service.RentService;

@WebMvcTest(RentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class RentControllerTest {
      
   @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentService rentService;

    @MockBean
    private JWTservice jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllRents() throws Exception {
        String token = "dummyToken";

        Car car = new Car("Toyota", "Corolla", "Sedan", "1-ABC-123", 5, 2, true, false, "owner@example.com");

        // Mock JWT and RentalService behavior
        Rental rental = new Rental(java.sql.Date.valueOf("2025-01-01"), java.sql.Date.valueOf("2025-01-02"), "Leuven", "Tiensestraat", 3000, "0467338231", "test@renter.com", car);
        Rental rental1 = new Rental(java.sql.Date.valueOf("2025-01-03"), java.sql.Date.valueOf("2025-01-04"), "Brussel", "Leuvensestteenweg", 1000, "0467338231", "aaron@test.com", car);
     
        List<Rent> rents = Arrays.asList(
            new Rent("0456723411", "renter@mail.com", "12345678901", java.sql.Date.valueOf("2000-01-01"), "123456789", Rent.Status.Pending, rental),
            new Rent("0486367728", "renter2@mail.com", "10987654321", java.sql.Date.valueOf("1987-11-06"), "987654321", Rent.Status.Confirmed, rental1)
        );

        doNothing().when(jwtService).verifyToken("dummytoken");
        when(jwtService.verifyRole(anyString(), anyString())).thenReturn(true);
        when(rentService.getAllRents(token)).thenReturn(rents);

        mockMvc.perform(get("/rents/all")
        .header("Authorization", "Bearer " + token)).
        andExpect(status().isOk())
        .andExpect(jsonPath("$[0].renterPhoneNumber").value("0456723411"))
        .andExpect(jsonPath("$[1].renterPhoneNumber").value("0486367728"))
        .andExpect(jsonPath("$[0].renterMail").value("renter@mail.com"))
        .andExpect(jsonPath("$[1].renterMail").value("renter2@mail.com"));
    }


    @Test
    void testAddRentalSuccess() throws Exception {
        String token = "dummyToken";

        // Mock JWT behavior
        doNothing().when(jwtService).verifyToken(token);
        when(jwtService.verifyRole(anyString(), anyString())).thenReturn(true);

        RentRequest rentRequest = new RentRequest();
        rentRequest.setRenterPhoneNumber("0456723411");
        rentRequest.setRenterMail("renter@mail.com");
        rentRequest.setNationalRegisterNumber("12345678901");
        rentRequest.setBirthDate(java.sql.Date.valueOf("2000-01-01"));
        rentRequest.setDrivingLicenseNumber("123456789");


        // Perform POST /rents/add
        mockMvc.perform(post("/rents/create/1")
        .header("Authorization", "Bearer mockToken")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(rentRequest)))
            .andExpect(status().isOk());


    }

    // generate test for cancelRent
    // @Test
    // void testCancelRent() throws Exception {

    //     Car car = new Car("Toyota", "Corolla", "Sedan", "1-ABC-123", 5, 2, true, false, "owner@example.com");

    //     // Mock JWT and RentalService behavior
    //     Rental rental = new Rental(java.sql.Date.valueOf("2025-01-01"), java.sql.Date.valueOf("2025-01-02"), "Leuven", "Tiensestraat", 3000, "0467338231", "test@renter.com", car);
     
    //     Rent rent = new Rent("0456723411", "renter@mail.com", "12345678901", java.sql.Date.valueOf("2000-01-01"), "123456789", Rent.Status.Pending, rental),
        

    //     doNothing().when(jwtService).verifyToken(anyString());
    //     when(jwtService.verifyRole(anyString(), anyString())).thenReturn(true);
    //     when(rentService.cancelRent(1L)).thenReturn(rent);


    //     // Perform DELETE /rents/cancel/{id}
    //     mockMvc.perform(post("/rents/cancel/1")
    //     .header("Authorization", "Bearer mockToken" ))
    //     .andExpect(status().isOk());

    //     verify(rentService, times(1)).cancelRent(1L);
    // }   


    @Test
    void testUnauthorizedRoleAccess() throws Exception {
        // Mock JWT behavior
        doNothing().when(jwtService).verifyToken(anyString());
        when(jwtService.verifyRole(anyString(), anyString())).thenReturn(false);

        // Perform GET /rentals/all with unauthorized role
        mockMvc.perform(get("/rents/all")
                        .header("Authorization", "Bearer mockToken"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.role").value("role.invalid"));
    }

}
