package be.ucll.se.aaron_abbey_backend.bdd.steps;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import be.ucll.se.aaron_abbey_backend.AaronAbbeyBackendApplication;
import be.ucll.se.aaron_abbey_backend.DTO.CarRequest;
import be.ucll.se.aaron_abbey_backend.authentication.JWT.JWTservice;
import be.ucll.se.aaron_abbey_backend.model.Car;
import be.ucll.se.aaron_abbey_backend.model.User;
import be.ucll.se.aaron_abbey_backend.repository.CarRepo;
import be.ucll.se.aaron_abbey_backend.repository.UserRepo;
import be.ucll.se.aaron_abbey_backend.service.CarService;
import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = AaronAbbeyBackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @Transactional
@ActiveProfiles("test")
public class CarSteps {

    @Autowired
    private WebTestClient client;

    @Mock
    private CarRepo carRepository;

   
    @MockBean
    private UserRepo userRepository;


    @Autowired
    private JWTservice jwtService;


    @InjectMocks
    private CarService carService;

    


    private WebTestClient.ResponseSpec response;

    private String jwtOwnerToken;

    private String jwtAdminToken;

    private String jwtRenterToken;

    private Car car;

    private CarRequest carRequest;
    private CarRequest carRequest2;
    private CarRequest carRequest3;
    private CarRequest renter;
    private CarRequest tokenless;
    

    @Before
    public void setUp() {

        User admin = new User("admin", "admin", "admin@mail.com", User.Role.Admin);
        User renter = new User("renter", "renter", "renter@mail.com", User.Role.Renter);
        User owner1 = new User("owner1", "owner1", "owner@car.com", User.Role.Owner);
        userRepository.save(admin);
        userRepository.save(renter);
        userRepository.save(owner1);


        car = new Car("Audi", "IX", "SUV", "1-ABC-123", 5, 1, true, true, "owner@car.com");
    }



    @Given("I have an owner token")
    public void i_have_an_owner_token() throws ServiceException {
       // mock validate token
       carRepository.deleteAll();
       jwtOwnerToken = jwtService.createToken("owner@car.com", "Owner", 1L);

    }



    @When("I add a car")
    public void i_add_a_car_with_brand_model_type_license_plate_number_of_seats_number_of_child_seats_folding_rear_seats_tow_bar() throws ServiceException {
        carRequest = new CarRequest();
        carRequest.setBrand("Toyota");
        carRequest.setModel("Corolla");
        carRequest.setType("Sedan");
        carRequest.setLicensePlate("1-ABC-123");
        carRequest.setNumberOfSeats(5);
        carRequest.setNumberOfChildSeats(2);
        carRequest.setHaveFoldingRearSeats(true);
        carRequest.setHasTowBar(false);

        // mock add car
        when(carRepository.findByLicensePlate(carRequest.getLicensePlate())).thenReturn(null);

        response = client.post()
                         .uri("/cars/add")
                         .header("Authorization", "Bearer " + jwtOwnerToken)
                         .bodyValue(carRequest)
                         .exchange();

    }

    @Then("the car is added")
    public void the_car_is_added() {
        response.expectStatus().isOk();
    }

    @Given("I have an admin token")
    public void i_have_an_admin_token() throws ServiceException {
        // mock validate token
        carRepository.deleteAll();

        jwtAdminToken = jwtService.createToken("admin@mail.com", "Admin", 2L);

    }

    @Given("I have a renter token")
    public void i_have_a_renter_token() throws ServiceException {
        // mock validate token
        carRepository.deleteAll();
        jwtRenterToken = jwtService.createToken("renter@mail.com", "Renter", 3L);
    }

    


    @When("I add a car for the first time")
    public void i_add_a_car_for_() throws ServiceException {
        carRequest2 = new CarRequest();
        carRequest2.setBrand("Toyota");
        carRequest2.setModel("Corolla");
        carRequest2.setType("Sedan");
        carRequest2.setLicensePlate("1-ABC-123");
        carRequest2.setNumberOfSeats(5);
        carRequest2.setNumberOfChildSeats(2);
        carRequest2.setHaveFoldingRearSeats(true);
        carRequest2.setHasTowBar(false);

        // mock add car
        when(carRepository.findByLicensePlate(carRequest2.getLicensePlate())).thenReturn(null);

        client.post()
                         .uri("/cars/add")
                         .header("Authorization", "Bearer " + jwtOwnerToken)
                         .bodyValue(carRequest2)
                         .exchange();

    }

    
    @And("I add a same car with the same license plate")
    public void i_add_a_same_car_with_the_same_license_plate() throws ServiceException {
        carRequest3 = new CarRequest();
        carRequest3.setBrand("Audi");
        carRequest3.setModel("IX");
        carRequest3.setType("SUV");
        carRequest3.setLicensePlate("1-ABC-123");
        carRequest3.setNumberOfSeats(7);
        carRequest3.setNumberOfChildSeats(1);
        carRequest3.setHaveFoldingRearSeats(true);
        carRequest3.setHasTowBar(true);

        // mock add car
        when(carRepository.findByLicensePlate(carRequest3.getLicensePlate())).thenReturn(car);

        response = client.post()
                         .uri("/cars/add")
                         .header("Authorization", "Bearer " + jwtOwnerToken)
                         .bodyValue(carRequest3)
                         .exchange();
    }




   @When("I add a car with a renter")
    public void i_add_a_car_with_a_renter() throws ServiceException {
        renter = new CarRequest();
        renter.setBrand("Toyota");
        renter.setModel("Corolla");
        renter.setType("Sedan");
        renter.setLicensePlate("1-ABC-123");
        renter.setNumberOfSeats(5);
        renter.setNumberOfChildSeats(2);
        renter.setHaveFoldingRearSeats(true);
        renter.setHasTowBar(false);

        // mock add car
        when(carRepository.findByLicensePlate(renter.getLicensePlate())).thenReturn(null);

        response = client.post()
                         .uri("/cars/add")
                         .header("Authorization", "Bearer " + jwtRenterToken)
                         .bodyValue(renter)
                         .exchange();
    }


    @When("I add a car without a token")
    public void i_add_a_car_without_a_token() {
        tokenless = new CarRequest();
        tokenless.setBrand("Toyota");
        tokenless.setModel("Corolla");
        tokenless.setType("Sedan");
        tokenless.setLicensePlate("1-ABC-123");
        tokenless.setNumberOfSeats(5);
        tokenless.setNumberOfChildSeats(2);
        tokenless.setHaveFoldingRearSeats(true);
        tokenless.setHasTowBar(false);



        response = client.post()
                         .uri("/cars/add")
                         .bodyValue(tokenless)
                         .exchange();
    }

    @Then("an error is thrown")
    public void an_error_is_thrown() {
        response.expectStatus().isForbidden();
    }

    @Then("a bad request is thrown")
    public void a_bad_request_is_thrown() {
        response.expectStatus().isBadRequest();
    }
}
