package be.ucll.se.aaron_abbey_backend.integration.http;

import be.ucll.se.aaron_abbey_backend.DTO.CarRequest;
import be.ucll.se.aaron_abbey_backend.model.Car;
import be.ucll.se.aaron_abbey_backend.service.CarService;
import be.ucll.se.aaron_abbey_backend.authentication.JWT.JWTservice;
import be.ucll.se.aaron_abbey_backend.controller.CarController;

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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CarController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @MockBean
    private JWTservice jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllCars() throws Exception {
        // Mock JWT verification and token setup
        String token = "dummyToken";
        doNothing().when(jwtService).verifyToken(token);
        
        // Mock role verification for both Admin and Owner checks
        when(jwtService.verifyRole(token, "Admin")).thenReturn(true);  // Not admin
        when(jwtService.verifyRole(token, "Owner")).thenReturn(false);   // But is owner
        
        // Mock test data
        List<Car> cars = Arrays.asList(
            new Car("Toyota", "Corolla", "Sedan", "1-ABC-123", 5, 2, true, false, "owner1@test.com"),
            new Car("Honda", "Civic", "Sedan", "2-DEF-456", 4, 1, false, true, "owner2@test.com")
        );
        when(carService.getAllCars(token)).thenReturn(cars);

        // Perform GET request and verify
        mockMvc.perform(get("/cars/all")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brand").value("Toyota"))
                .andExpect(jsonPath("$[1].brand").value("Honda"));

        // Verify service calls
        verify(jwtService, times(1)).verifyToken(token);
        verify(jwtService, times(1)).verifyRole(token, "Admin");
        verify(carService, times(1)).getAllCars(token);
    }

    @Test
    void testAddCar() throws Exception {
        // Mock JWT verification
        String token = "dummyToken";
        doNothing().when(jwtService).verifyToken(token);        
        when(jwtService.verifyRole(token, "Admin")).thenReturn(true);
        when(jwtService.getSubjectFromToken(token)).thenReturn("owner@test.com");

        // Mock data
        CarRequest carRequest = new CarRequest();
        carRequest.setBrand("Ford");
        carRequest.setModel("Focus");
        carRequest.setType("Hatchback");
        carRequest.setLicensePlate("3-GHI-789");
        carRequest.setNumberOfSeats(5);
        carRequest.setNumberOfChildSeats(0);
        carRequest.setHaveFoldingRearSeats(false);
        carRequest.setHasTowBar(true);
        Car savedCar = new Car("Ford", "Focus", "Hatchback", "3-GHI-789", 5, 0, false, true, "owner@test.com");

        when(carService.addCar(eq(carRequest), eq("owner@test.com"))).thenReturn(savedCar);

        // Perform POST /cars/add
        mockMvc.perform(post("/cars/add")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carRequest)))
                .andExpect(status().isOk());
                


    verify(carService, times(1)).addCar(refEq(carRequest), eq("owner@test.com"));   
 }

    @Test
    void testFindCarById() throws Exception {
        // Mock JWT verification
        String token = "dummyToken";
        doNothing().when(jwtService).verifyToken(token);
        when(jwtService.verifyRole(token, "Admin")).thenReturn(true);

        // Mock data
        long carId = 1L;
        Car car = new Car("BMW", "X5", "SUV", "4-JKL-101", 7, 1, true, true, "owner@test.com");
        when(carService.findCarById(carId)).thenReturn(car);

        // Perform GET /cars/find/{id}
        mockMvc.perform(get("/cars/find/{id}", carId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("BMW"))
                .andExpect(jsonPath("$.type").value("SUV"));

        verify(carService, times(1)).findCarById(carId);
    }

    @Test
    void testDeleteCar() throws Exception {
        // Mock JWT verification
        String token = "dummyToken";
        doNothing().when(jwtService).verifyToken(token);
        when(jwtService.verifyRole(token, "Admin")).thenReturn(true);

        long carId = 1L;

        // Perform DELETE /cars/delete/{id}
        mockMvc.perform(delete("/cars/delete/{id}", carId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        verify(carService, times(1)).deleteCar(carId);
    }

    @Test
    void testInvalidRole() throws Exception {
        // Mock JWT verification
        String token = "dummyToken";
        doNothing().when(jwtService).verifyToken(token);
        when(jwtService.verifyRole(token, "Admin")).thenReturn(false);
        when(jwtService.verifyRole(token, "Owner")).thenReturn(false);

        // Perform GET /cars/all
        mockMvc.perform(get("/cars/all")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.role").value("role.invalid"));
    }
}
