package be.ucll.se.aaron_abbey_backend.e2e;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import be.ucll.se.aaron_abbey_backend.DTO.CarRequest;
import be.ucll.se.aaron_abbey_backend.DTO.RentRequest;
import be.ucll.se.aaron_abbey_backend.DTO.RentalRequest;
import be.ucll.se.aaron_abbey_backend.authentication.JWT.JWTservice;
import be.ucll.se.aaron_abbey_backend.authentication.controller.AuthenticationController;
import be.ucll.se.aaron_abbey_backend.model.User;
import be.ucll.se.aaron_abbey_backend.service.CarService;
import be.ucll.se.aaron_abbey_backend.service.RentService;
import be.ucll.se.aaron_abbey_backend.service.RentalService;
import be.ucll.se.aaron_abbey_backend.service.UserService;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class RentControllerE2ETest {

    
        @Autowired
        private MockMvc mockMvc;
    
        @Autowired
        private ObjectMapper objectMapper;
    
        @Autowired
        private RentService rentService;

        @Autowired
        private CarService carService;

        @Autowired
        private RentalService rentalService;
    
        @Autowired
        private JWTservice jwtService;

        @Autowired
        private UserService userService;
        
        private String adminToken;
        private String renterToken;

        private User renter;
    
        @BeforeEach
        public void setUp() {

            renter = new  User("renter", "renter123", "renter@test.com", User.Role.Renter);

            userService.createUser(renter);
                 
            adminToken = jwtService.createToken("admin@test.com", "Admin", 1L);
            renterToken = jwtService.createToken("renter@test.com", "Renter", 2L);
        }


        @Test
        public void testGetAllRents() throws Exception {
            // Mock JWT verification and token setup
            mockMvc.perform(get("/rents/all")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
        }


        @Test
        public void testGetAllRentsWithRents() throws Exception {

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

           Long rentalId = rentalService.addRental(rentalRequest, carId).getId();

            RentRequest rentRequest = new RentRequest();
            rentRequest.setBirthDate(java.sql.Date.valueOf(LocalDate.now().minusYears(20)));
            rentRequest.setDrivingLicenseNumber("123456789");
            rentRequest.setNationalRegisterNumber("12345678910");
            rentRequest.setRenterPhoneNumber("0455627728");
            rentRequest.setRenterMail("renter@test.com");
            

            rentService.addRent(rentRequest, rentalId);

            
            // Mock JWT verification and token setup
            mockMvc.perform(get("/rents/all")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].renterMail").value("renter@test.com"))
                .andExpect(jsonPath("$[0].renterPhoneNumber").value("0455627728"))
                .andExpect(jsonPath("$[0].nationalRegisterNumber").value("12345678910"))
                .andExpect(jsonPath("$[0].drivingLicenseNumber").value("123456789"))
                .andExpect(jsonPath("$[0].birthDate").value(LocalDate.now().minusYears(20).toString()))
                .andExpect(jsonPath("$[0].status").value("Pending"));
        }




        @Test
        void testAddRent() throws Exception {
            // Mock JWT verification

            CarRequest carRequest = new CarRequest();
            carRequest.setBrand("Mercedes");
            carRequest.setModel("C-Class");
            carRequest.setType("Sedan");
            carRequest.setLicensePlate("3-DEF-456");
            carRequest.setHasTowBar(false);
            carRequest.setHaveFoldingRearSeats(true);
            carRequest.setNumberOfChildSeats(2);
            carRequest.setNumberOfSeats(5);


            Long carId = carService.addCar(carRequest, "owner@test.com").getId();

            RentalRequest rentalRequest = new RentalRequest();
            rentalRequest.setStartDate(java.sql.Date.valueOf(LocalDate.now().plusDays(1)));
            rentalRequest.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusDays(7)));
            rentalRequest.setCity("Gent");
            rentalRequest.setStreet("Oude Markt");
            rentalRequest.setPostalCode(2000);
            rentalRequest.setPhoneNumber("0455627728");
            rentalRequest.setEmail("renter@mail.com");

           Long rentalId = rentalService.addRental(rentalRequest, carId).getId();

            RentRequest rentRequest = new RentRequest();
            rentRequest.setBirthDate(java.sql.Date.valueOf(LocalDate.now().minusYears(20)));
            rentRequest.setDrivingLicenseNumber("123456789");
            rentRequest.setNationalRegisterNumber("12345678910");
            rentRequest.setRenterPhoneNumber("0455627728");
            rentRequest.setRenterMail("renter@test.com");
            
            
            // Mock JWT verification and token setup
            mockMvc.perform(post("/rents/create/" + rentalId)
                .header("Authorization", "Bearer " + renterToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rentRequest)))
                .andExpect(status().isOk());
        }

        @Test 
        void cancelRent() throws Exception {
            // Mock JWT verification
            CarRequest carRequest = new CarRequest();
            carRequest.setBrand("Ferrari");
            carRequest.setModel("Testarossa");
            carRequest.setType("Sportscar");
            carRequest.setLicensePlate("5-XYZ-789");
            carRequest.setHasTowBar(false);
            carRequest.setHaveFoldingRearSeats(true);
            carRequest.setNumberOfChildSeats(0);
            carRequest.setNumberOfSeats(3);


            Long carId = carService.addCar(carRequest, "owner@test.com").getId();

            RentalRequest rentalRequest = new RentalRequest();
            rentalRequest.setStartDate(java.sql.Date.valueOf(LocalDate.now().plusDays(1)));
            rentalRequest.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusDays(7)));
            rentalRequest.setCity("Brussels");
            rentalRequest.setStreet("Stationstraat");
            rentalRequest.setPostalCode(1000);
            rentalRequest.setPhoneNumber("0455627728");
            rentalRequest.setEmail("renter@mail.com");

           Long rentalId = rentalService.addRental(rentalRequest, carId).getId();

            RentRequest rentRequest = new RentRequest();
            rentRequest.setBirthDate(java.sql.Date.valueOf(LocalDate.now().minusYears(20)));
            rentRequest.setDrivingLicenseNumber("123456789");
            rentRequest.setNationalRegisterNumber("12345678910");
            rentRequest.setRenterPhoneNumber("0455627728");
            rentRequest.setRenterMail("renter@test.com");
            

            rentService.addRent(rentRequest, rentalId);

            mockMvc.perform(delete("/rents/cancel/" + rentalId)
                .header("Authorization", "Bearer " + renterToken))
                .andExpect(status().isOk());
        }
    
}


