package be.ucll.se.aaron_abbey_backend.integration.http;

import be.ucll.se.aaron_abbey_backend.DTO.RentalRequest;
import be.ucll.se.aaron_abbey_backend.authentication.JWT.JWTservice;
import be.ucll.se.aaron_abbey_backend.controller.RentalController;
import be.ucll.se.aaron_abbey_backend.model.Car;
import be.ucll.se.aaron_abbey_backend.model.Rental;
import be.ucll.se.aaron_abbey_backend.service.RentalService;
import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RentalController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class RentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RentalService rentalService;

    @MockBean
    private JWTservice jwtService;

    @Test
    void testGetAllRentalsSuccess() throws Exception {

        String token = "dummyToken";
        
        Car car = new Car("Toyota", "Corolla", "Sedan", "1-ABC-123", 5, 2, true, false, "owner@example.com");

        // Mock JWT and RentalService behavior
        List<Rental> rentals = Arrays.asList(
                new Rental(java.sql.Date.valueOf("2025-01-01"), java.sql.Date.valueOf("2025-01-02"), "Leuven", "Tiensestraat", 3000, "0467338231", "test@renter.com", car),
                new Rental(java.sql.Date.valueOf("2025-01-03"), java.sql.Date.valueOf("2025-01-04"), "Brussel", "Leuvensestteenweg", 1000, "0467338231", "aaron@test.com", car)
        );
        doNothing().when(jwtService).verifyToken(token);
        when(jwtService.verifyRole(anyString(), anyString())).thenReturn(true);
        when(rentalService.getAllRentals()).thenReturn(rentals);

        // Perform GET /rentals/all
        mockMvc.perform(get("/rentals/all")
                        .header("Authorization", "Bearer mockToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value("Leuven"))
                .andExpect(jsonPath("$[0].street").value("Tiensestraat"))
                .andExpect(jsonPath("$[1].city").value("Brussel"))
                .andExpect(jsonPath("$[1].street").value("Leuvensestteenweg"));
    }

    @Test
    void testAddRentalSuccess() throws Exception {
        // Mock JWT behavior
        doNothing().when(jwtService).verifyToken(anyString());
        when(jwtService.verifyRole(anyString(), anyString())).thenReturn(true);

        RentalRequest rentalRequest = new RentalRequest();
        rentalRequest.setStartDate(java.sql.Date.valueOf("2025-01-01"));
        rentalRequest.setEndDate(java.sql.Date.valueOf("2025-01-02"));
        rentalRequest.setCity("Leuven");
        rentalRequest.setStreet("Tiensestraat");
        rentalRequest.setPostalCode(3000);
        rentalRequest.setPhoneNumber("0467338231");
        rentalRequest.setEmail("test@mail.com");

        // Perform POST /rentals/create/{carId}
        mockMvc.perform(post("/rentals/create/1")
                        .header("Authorization", "Bearer mockToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rentalRequest)))
                .andExpect(status().isOk());

        // Verify service call
        verify(rentalService, times(1)).addRental(any(RentalRequest.class), eq(1L));
    }

    @Test
    void testDeleteRentalSuccess() throws Exception {
        // Mock JWT behavior
        doNothing().when(jwtService).verifyToken(anyString());
        when(jwtService.verifyRole(anyString(), anyString())).thenReturn(true);

        // Perform DELETE /rentals/cancel/{id}
        mockMvc.perform(delete("/rentals/cancel/1")
                        .header("Authorization", "Bearer mockToken"))
                .andExpect(status().isOk());

        // Verify service call
        verify(rentalService, times(1)).deleteRental(1L);
    }

    @Test
    void testFindRentalByIdSuccess() throws Exception {

        Car car = new Car("Toyota", "Corolla", "Sedan", "1-ABC-123", 5, 2, true, false, "owner@example.com");

        // Mock JWT and RentalService behavior
        Rental rental = new Rental(java.sql.Date.valueOf("2025-01-03"), java.sql.Date.valueOf("2025-01-04"), "Leuven", "Tiensestraat", 3000, "0467338231", "aaronétest;com", car);
        doNothing().when(jwtService).verifyToken(anyString());
        when(jwtService.verifyRole(anyString(), anyString())).thenReturn(true);
        when(rentalService.getRentalById(1L)).thenReturn(rental);

        // Perform GET /rentals/find/{id}
        mockMvc.perform(get("/rentals/find/1")
                        .header("Authorization", "Bearer mockToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("Tiensestraat"))
                .andExpect(jsonPath("$.city").value("Leuven"))
                .andExpect(jsonPath("$.postalCode").value(3000));
            
    }

    @Test
    void testUnauthorizedRoleAccess() throws Exception {
        // Mock JWT behavior
        doNothing().when(jwtService).verifyToken(anyString());
        when(jwtService.verifyRole(anyString(), anyString())).thenReturn(false);

        // Perform GET /rentals/all with unauthorized role
        mockMvc.perform(get("/rentals/all")
                        .header("Authorization", "Bearer mockToken"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.role").value("role.invalid"));
    }

    @Test
    void testHandleValidationException() throws Exception {
        // Simulate a ServiceException
        doThrow(new ServiceException("field", "Invalid input")).when(rentalService).deleteRental(1L);

        // Perform DELETE /rentals/cancel/{id}
        mockMvc.perform(delete("/rentals/cancel/1")
                        .header("Authorization", "Bearer mockToken"))
                .andExpect(status().isBadRequest());
            
            }
}
