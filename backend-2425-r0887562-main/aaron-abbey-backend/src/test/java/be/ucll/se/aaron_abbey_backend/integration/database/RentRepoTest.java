package be.ucll.se.aaron_abbey_backend.integration.database;

import be.ucll.se.aaron_abbey_backend.model.Car;
import be.ucll.se.aaron_abbey_backend.model.Rent;
import be.ucll.se.aaron_abbey_backend.model.Rental;
import be.ucll.se.aaron_abbey_backend.repository.CarRepo;
import be.ucll.se.aaron_abbey_backend.repository.RentRepo;
import be.ucll.se.aaron_abbey_backend.repository.RentalRepo;
import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class RentRepoTest {

    @Autowired
    private RentRepo rentRepo;

    @Autowired
    private CarRepo carRepo;

    @Autowired
    private RentalRepo rentalRepo;

    private Rent rent1;
    private Rent rent2;
    private Rental rental;
    private Car car;

    @BeforeEach
    void setUp() throws ServiceException {
        // Create a Car
        car = new Car();
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setType("Sedan");
        car.setLicensePlate("1-ABC-123");
        car.setOwnerEmail("owner@example.com");
        car.setNumberOfSeats(5);
        car.setNumberOfChildSeats(2);
        car.setHaveFoldingRearSeats(true);
        car.setHasTowBar(false);

        carRepo.save(car);


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1); // Add 1 day to today
        Date startDate = calendar.getTime();

        // Set the end date to three days after tomorrow
        calendar.add(Calendar.DAY_OF_YEAR, 3); // Add 3 more days
        Date endDate = calendar.getTime();

       

        // Create a Rental
        rental = new Rental();
        rental.setStartDate(startDate);
        rental.setEndDate(endDate);
        rental.setCity("Brussels");
        rental.setStreet("Main Street");
        rental.setPostalCode(1000);
        rental.setPhoneNumber("0123456789");
        rental.setEmail("rental@example.com");
        rental.setCar(car);

        rentalRepo.save(rental);

        // Create Rent objects
        rent1 = new Rent();
        rent1.setRenterMail("renter1@example.com");
        rent1.setRenterPhoneNumber("0456789012");
        rent1.setNationalRegisterNumber("12345678901");
        rent1.setBirthDate(new Date(0));
        rent1.setDrivingLicenseNumber("987654321");
        rent1.setStatus(Rent.Status.Pending);
        rent1.setRental(rental);

        rent2 = new Rent();
        rent2.setRenterMail("renter2@example.com");
        rent2.setRenterPhoneNumber("0456789023");
        rent2.setNationalRegisterNumber("10987654321");
        rent2.setBirthDate(new Date(0));
        rent2.setDrivingLicenseNumber("123456789");
        rent2.setStatus(Rent.Status.Confirmed);
        rent2.setRental(rental);

        // Save Rent objects
        rentRepo.save(rent1);
        rentRepo.save(rent2);
    }

    @Test
    void testFindById() {
        Optional<Rent> foundRent = rentRepo.findById(rent1.getId());
        assertTrue(foundRent.isPresent());
        assertEquals("renter1@example.com", foundRent.get().getRenterMail());
    }

    @Test
    void testFindAllByRenterMail() {
        List<Rent> rents = rentRepo.findAllByRenterMail("renter1@example.com");
        assertEquals(1, rents.size());
        assertEquals("renter1@example.com", rents.get(0).getRenterMail());
    }

    @Test
    void testFindAllByRentalId() {
        List<Rent> rents = rentRepo.findAllByRentalId(rental.getId());
        assertEquals(2, rents.size());
        assertTrue(rents.stream().anyMatch(rent -> rent.getRenterMail().equals("renter1@example.com")));
        assertTrue(rents.stream().anyMatch(rent -> rent.getRenterMail().equals("renter2@example.com")));
    }
}
