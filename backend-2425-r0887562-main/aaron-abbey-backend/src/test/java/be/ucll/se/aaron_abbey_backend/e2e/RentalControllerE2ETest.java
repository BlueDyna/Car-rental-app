package be.ucll.se.aaron_abbey_backend.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import be.ucll.se.aaron_abbey_backend.DTO.CarRequest;
import be.ucll.se.aaron_abbey_backend.DTO.RentalRequest;
import be.ucll.se.aaron_abbey_backend.authentication.JWT.JWTservice;
import be.ucll.se.aaron_abbey_backend.controller.RentalController;
import be.ucll.se.aaron_abbey_backend.model.Car;
import be.ucll.se.aaron_abbey_backend.model.Rental;
import be.ucll.se.aaron_abbey_backend.service.CarService;
import be.ucll.se.aaron_abbey_backend.service.RentalService;
import jakarta.transaction.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional 
public class RentalControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private CarService carService;

    @Autowired
    private JWTservice jwtService;

    private ObjectMapper objectMapper;

     private String adminToken;
    private String ownerToken;
    private String invalidToken;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();

        adminToken = jwtService.createToken("admin@test.com", "Admin", 1L);
        ownerToken = jwtService.createToken("owner@test.com", "Owner", 2L);
        invalidToken = "invalid.token.here";
      
    }

    @Test
    public void testGetAllRentals_AdminAuthorized() throws Exception {

        // Perform request and verify
        mockMvc.perform(get("/rentals/all")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetAllRentals_AdminAuthorized_with_rental() throws Exception {

        CarRequest carRequest = new CarRequest();
        carRequest.setBrand("Toyota");
        carRequest.setModel("Corolla");
        carRequest.setType("Sedan");
        carRequest.setLicensePlate("1-ABC-123");
        carRequest.setHasTowBar(false);
        carRequest.setHaveFoldingRearSeats(true);
        carRequest.setNumberOfChildSeats(0);
        carRequest.setNumberOfSeats(5);


        Long carId = carService.addCar(carRequest, "owner@test.com").getId();

        RentalRequest rentalRequest = new RentalRequest();
        rentalRequest.setStartDate(java.sql.Date.valueOf(LocalDate.now().plusDays(1)));
        rentalRequest.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusDays(7)));
        rentalRequest.setCity("Leuven");
        rentalRequest.setStreet("Tiensestraat");
        rentalRequest.setPostalCode(3000);
        rentalRequest.setPhoneNumber("0455627728");
        rentalRequest.setEmail("renter@mail.com");

        rentalService.addRental(rentalRequest, carId);

        // Perform request and verify
        mockMvc.perform(get("/rentals/all")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value("Leuven"));
    }

   

    @Test
    public void testAddRental_OwnerAuthorized() throws Exception {
        // Prepare test data
        CarRequest carRequest = new CarRequest();
        carRequest.setBrand("Audi");
        carRequest.setModel("IX");
        carRequest.setType("Golf");
        carRequest.setLicensePlate("1-ABC-123");
        carRequest.setHasTowBar(false);
        carRequest.setHaveFoldingRearSeats(true);
        carRequest.setNumberOfChildSeats(0);
        carRequest.setNumberOfSeats(5);

        Long carId = carService.addCar(carRequest, "owner@test.com").getId();



        RentalRequest rentalRequest = new RentalRequest();
        rentalRequest.setStartDate(java.sql.Date.valueOf(LocalDate.now().plusDays(1)));
        rentalRequest.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusDays(7)));
        rentalRequest.setCity("Leuven");
        rentalRequest.setStreet("Tiensestraat");
        rentalRequest.setPostalCode(3000);
        rentalRequest.setPhoneNumber("0455627728");
        rentalRequest.setEmail("renter@mail.com");


        // Perform request and verify
        mockMvc.perform(post("/rentals/create/" + carId)
                .header("Authorization", "Bearer " + ownerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rentalRequest)))
                .andExpect(status().isOk());

    }

    @Test
    public void testAddRental_UnauthorizedRole() throws Exception {
        String renterToken = jwtService.createToken("user@test.com", "Renter", 3L);

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



        // Prepare test data
        RentalRequest rentalRequest = new RentalRequest();
        rentalRequest.setStartDate(java.sql.Date.valueOf(LocalDate.now()));
        rentalRequest.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusDays(7)));
        rentalRequest.setCity("Leuven");
        rentalRequest.setStreet("Tiensestraat");
        rentalRequest.setPostalCode(3000);
        rentalRequest.setPhoneNumber("0455627728");
        rentalRequest.setEmail("renter@mail.com");


        // Perform request and verify unauthorized access
        mockMvc.perform(post("/rentals/create/1")
                .header("Authorization", "Bearer " + renterToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rentalRequest)))
                .andExpect(status().isBadRequest());
    }

    // @Test
    // public void testDeleteRental_AdminAuthorized() throws Exception {

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

    //     RentalRequest rentalRequest = new RentalRequest();
    //     rentalRequest.setStartDate(java.sql.Date.valueOf(LocalDate.now()));
    //     rentalRequest.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusDays(7)));
    //     rentalRequest.setCity("Leuven");
    //     rentalRequest.setStreet("Tiensestraat");
    //     rentalRequest.setPostalCode(3000);
    //     rentalRequest.setPhoneNumber("0455627728");
    //     rentalRequest.setEmail("renter@mail.com");

    //     rentalService.addRental(rentalRequest, 1L);



    //     // Perform request and verify
    //     mockMvc.perform(delete("/rentals/cancel/1")
    //             .header("Authorization", "Bearer " + adminToken))
    //             .andExpect(status().isOk());

    // }

    @Test
    public void testDeleteRental_UnauthorizedRole() throws Exception {

        String renterToken = jwtService.createToken("user@test.com", "Renter", 3L);


        // Perform request and verify unauthorized access
        mockMvc.perform(delete("/rentals/cancel/1")
                .header("Authorization", "Bearer " + renterToken))
                .andExpect(status().isBadRequest());
    }

    // @Test
    // public void testFindRentalById_AdminAuthorized() throws Exception {

    //     CarRequest carRequest = new CarRequest();
    //     carRequest.setBrand("Toyota");
    //     carRequest.setModel("Corolla");
    //     carRequest.setType("Sedan");
    //     carRequest.setLicensePlate("1-ABC-123");
    //     carRequest.setHasTowBar(false);
    //     carRequest.setHaveFoldingRearSeats(true);
    //     carRequest.setNumberOfChildSeats(0);
    //     carRequest.setNumberOfSeats(5);
      
    //     Long carId = carService.addCar(carRequest, "owner@test.com").getId();

    //     RentalRequest rentalRequest = new RentalRequest();
    //     rentalRequest.setStartDate(java.sql.Date.valueOf(LocalDate.now()));
    //     rentalRequest.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusDays(7)));
    //     rentalRequest.setCity("Leuven");
    //     rentalRequest.setStreet("Tiensestraat");
    //     rentalRequest.setPostalCode(3000);
    //     rentalRequest.setPhoneNumber("0455627728");
    //     rentalRequest.setEmail("renter@mail.com");

    //     rentalService.addRental(rentalRequest, carId);

        
    //     // Perform request and verify
    //     mockMvc.perform(get("/rentals/find/" + carId)
    //             .header("Authorization", "Bearer " + adminToken))
    //             .andExpect(status().isOk())
    //             .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    //             .andExpect(jsonPath("$.id").value(1));

    // }

    @Test
    public void testInvalidTokenFormat() throws Exception {
        // Test with invalid token format
        mockMvc.perform(get("/rentals/all")
                .header("Authorization", "Invalid Token Format"))
                .andExpect(status().isForbidden());
    }
}