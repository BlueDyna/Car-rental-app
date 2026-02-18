package be.ucll.se.aaron_abbey_backend.e2e;

import be.ucll.se.aaron_abbey_backend.AaronAbbeyBackendApplication;
import be.ucll.se.aaron_abbey_backend.DTO.CarRequest;
import be.ucll.se.aaron_abbey_backend.authentication.JWT.JWTservice;
import be.ucll.se.aaron_abbey_backend.model.Car;
import be.ucll.se.aaron_abbey_backend.service.CarService;
import jakarta.transaction.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional 
public class CarControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JWTservice jwtService;

    @Autowired
    private CarService carService;

    private String adminToken;
    private String ownerToken;
    private String invalidToken;

    @BeforeEach
    public void setup() {
        // Generate test tokens for different roles
        adminToken = jwtService.createToken("admin@test.com", "Admin", 1L);
        ownerToken = jwtService.createToken("owner@test.com", "Owner", 2L);
        invalidToken = "invalid.token.here";
    }

    @Test
    public void testGetAllCarsWithAdminAccess() throws Exception {
        mockMvc.perform(get("/cars/all")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetAllCarsWithAdminAccessWithCar() throws Exception {

        Car car = new Car("Audi", "IX", "SUV", "3-OOP-883", 0, 0, false, false, "test@example.com");

        mockMvc.perform(post("/cars/add")
                .header("Authorization", "Bearer " + adminToken)
                .content(new ObjectMapper().writeValueAsString(car))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Audi"));


        mockMvc.perform(get("/cars/all")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brand").value("Audi"));
    }

    @Test
    public void testAddCarWithOwnerAccess() throws Exception {
        CarRequest carRequest = new CarRequest();
        carRequest.setBrand("Toyota");
        carRequest.setModel("Corolla");
        carRequest.setType("Sedan");
        carRequest.setLicensePlate("1-ABC-123");
        carRequest.setHasTowBar(false);
        carRequest.setHaveFoldingRearSeats(true);
        carRequest.setNumberOfChildSeats(0);
        carRequest.setNumberOfSeats(5);

        mockMvc.perform(post("/cars/add")
                .header("Authorization", "Bearer " + ownerToken)
                .content(new ObjectMapper().writeValueAsString(carRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Toyota"));
    }

    @Test
    public void testAddCarWithInvalidRole() throws Exception {
        CarRequest carRequest = new CarRequest();
        carRequest.setBrand("Toyota");
        carRequest.setModel("Corolla");
        carRequest.setType("Sedan");
        carRequest.setLicensePlate("1-ABC-123");
        carRequest.setHasTowBar(false);
        carRequest.setHaveFoldingRearSeats(true);
        carRequest.setNumberOfChildSeats(0);
        carRequest.setNumberOfSeats(5);


        // Generate a regular user token without Admin or Owner role
        String userToken = jwtService.createToken("user@test.com", "Renter", 3L);

        mockMvc.perform(post("/cars/add")
                .header("Authorization", "Bearer " + userToken)
                .content(new ObjectMapper().writeValueAsString(carRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // @Test
    // public void testFindCarByIdWithAdminAccess() throws Exception {
        
    //     CarRequest carRequest = new CarRequest();
    //     carRequest.setBrand("Toyota");
    //     carRequest.setModel("Corolla");
    //     carRequest.setType("Sedan");
    //     carRequest.setLicensePlate("1-ABC-123");
    //     carRequest.setHasTowBar(false);
    //     carRequest.setHaveFoldingRearSeats(true);
    //     carRequest.setNumberOfChildSeats(0);
    //     carRequest.setNumberOfSeats(5);


    //     carService.addCar(carRequest, "random@test.com");


    //     // Assume a car with ID 1 exists in the test database
    //     mockMvc.perform(get("/cars/find/1")
    //             .header("Authorization", "Bearer " + adminToken)
    //             .contentType(MediaType.APPLICATION_JSON))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.id").value(1))
    //             .andExpect(jsonPath("$.brand").value("Toyota"));
    // }

    @Test
    public void testDeleteCarWithOwnerAccess() throws Exception {

        // Add a car to the database with id 2
        CarRequest carRequest = new CarRequest();
        carRequest.setBrand("Toyota");
        carRequest.setModel("Corolla");
        carRequest.setType("Sedan");
        carRequest.setLicensePlate("1-ABC-123");
        carRequest.setHasTowBar(false); 
        carRequest.setHaveFoldingRearSeats(true);
        carRequest.setNumberOfChildSeats(0);
        carRequest.setNumberOfSeats(5);
        
        carService.addCar(carRequest, "random@test.com");



        // Assume a car with ID 2 exists in the test database
        mockMvc.perform(delete("/cars/delete/1")
                .header("Authorization", "Bearer " + ownerToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testUnauthorizedAccessWithInvalidToken() throws Exception {
        String userToken = jwtService.createToken("user@test.com", "Renter", 3L);


        mockMvc.perform(get("/cars/all")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}