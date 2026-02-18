package be.ucll.se.aaron_abbey_backend.model;


import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RentalTest {

    private static Validator validator;

    @BeforeAll
    public static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testRentalValidData() throws ServiceException {
        Car car = new Car("Toyota", "Corolla", "Sedan", "1-ABC-123", 5, 2, true, true, "owner@example.com");
        Date startDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60); // 1 hour from now
        Date endDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24); // 1 day from now

        Rental rental = new Rental(
                startDate,
                endDate,
                "Leuven",
                "Main Street",
                3000,
                "0123456789",
                "customer@example.com",
                car
        );

        Set<ConstraintViolation<Rental>> violations = validator.validate(rental);
        assertThat(violations).isEmpty();
        assertThat(rental.getCar()).isEqualTo(car);
    }

    @Test
    public void testRentalInvalidDates() throws ServiceException {
        Car car = new Car("Toyota", "Corolla", "Sedan", "1-ABC-123", 5, 2, true, true, "owner@example.com");
       // startdate after enddate
        Date startDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24); // 1 day from now
        Date endDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60); // 1 hour from now

        Rental rental = new Rental();
        rental.setStartDate(startDate);
        rental.setCar(car);
        assertThatThrownBy(() -> rental.setEndDate(endDate))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("End date must be after start date.");
    }

    @Test
    public void testRentalInvalidPhoneNumber() throws ServiceException {
        Car car = new Car("Toyota", "Corolla", "Sedan", "1-ABC-123", 5, 2, true, true, "owner@example.com");
        Date startDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60); // 1 hour from now
        Date endDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24); // 1 day from now

        Rental rental = new Rental(
                startDate,
                endDate,
                "Leuven",
                "Main Street",
                3000,
                "123456789", // Invalid phone number
                "customer@example.com",
                car
        );

        Set<ConstraintViolation<Rental>> violations = validator.validate(rental);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Phone number must start with 0 and be followed by 9 digits.");
    }

    @Test
    public void testRentalInvalidEmail() throws ServiceException {
        Car car = new Car("Toyota", "Corolla", "Sedan", "1-ABC-123", 5, 2, true, true, "owner@example.com");
        Date startDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60); // 1 hour from now
        Date endDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24); // 1 day from now

        Rental rental = new Rental(
                startDate,
                endDate,
                "Leuven",
                "Main Street",
                3000,
                "0123456789",
                "invalid-email", // Invalid email
                car
        );

        Set<ConstraintViolation<Rental>> violations = validator.validate(rental);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Email must be valid.");
    }

    @Test
    public void testRentalInvalidPostalCode() throws ServiceException {
        Car car = new Car("Toyota", "Corolla", "Sedan", "1-ABC-123", 5, 2, true, true, "owner@example.com");
        Date startDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60); // 1 hour from now
        Date endDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24); // 1 day from now

        Rental rental = new Rental(
                startDate,
                endDate,
                "Leuven",
                "Main Street",
                999, // Invalid postal code
                "0123456789",
                "customer@example.com",
                car
        );

        Set<ConstraintViolation<Rental>> violations = validator.validate(rental);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Postal code must be at least 1000.");
    }

    @Test
    public void testRentalDefaultConstructor() {
        Rental rental = new Rental();

        rental.setCity("Leuven");
        rental.setStreet("Main Street");
        rental.setPostalCode(3000);
        rental.setPhoneNumber("0123456789");
        rental.setEmail("customer@example.com");

        assertThat(rental.getCity()).isEqualTo("Leuven");
        assertThat(rental.getStreet()).isEqualTo("Main Street");
        assertThat(rental.getPostalCode()).isEqualTo(3000);
        assertThat(rental.getPhoneNumber()).isEqualTo("0123456789");
        assertThat(rental.getEmail()).isEqualTo("customer@example.com");
    }
}
