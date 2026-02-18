package be.ucll.se.aaron_abbey_backend.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;

import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RentTest {

    private static Validator validator;

    @BeforeAll
    public static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Car createValidCar() {
        return new Car(
                "Toyota",
                "Corolla",
                "Sedan",
                "1-ABC-123",
                5,
                2,
                true,
                false,
                "owner@example.com"
        );
    }

    private Rental createValidRental() throws ServiceException {
        Car car = createValidCar();
        return new Rental(
                new Date(System.currentTimeMillis()), // start date
                new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24), // end date
                "City",
                "Street",
                1234,
                "0123456789",
                "renter@example.com",
                car
        );
    }

    @Test
    public void testValidRent() throws ServiceException {
        Rental rental = createValidRental();
        Date birthDate = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 18); // 18 years ago

        Rent rent = new Rent(
                "0123456789",
                "renter@example.com",
                "12345678901",
                birthDate,
                "123456789",
                Rent.Status.Confirmed,
                rental
        );

        Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
        assertThat(violations).isEmpty();
    }

    @Test
    public void testInvalidPhoneNumber() throws ServiceException {
        Rental rental = createValidRental();
        Date birthDate = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 18);

        Rent rent = new Rent(
                "123456789", // Invalid phone number
                "renter@example.com",
                "12345678901",
                birthDate,
                "123456789",
                Rent.Status.Confirmed,
                rental
        );

        Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Phone number must start with 0 and be followed by 9 digits.");
    }

    @Test
    public void testInvalidEmail() throws ServiceException {
        Rental rental = createValidRental();
        Date birthDate = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 18);

        Rent rent = new Rent(
                "0123456789",
                "invalid-email", // Invalid email
                "12345678901",
                birthDate,
                "123456789",
                Rent.Status.Confirmed,
                rental
        );

        Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Email must be valid.");
    }

    @Test
    public void testInvalidNationalRegisterNumber() throws ServiceException {
        Rental rental = createValidRental();
        Date birthDate = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 18);

        Rent rent = new Rent(
                "0123456789",
                "renter@example.com",
                "12345", // Invalid national register number
                birthDate,
                "123456789",
                Rent.Status.Confirmed,
                rental
        );

        Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("National register number must be 11 digits.");
    }

    @Test
    public void testInvalidBirthDate() throws ServiceException {
        Rental rental = createValidRental();
        Date birthDate = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365); // 1 year in the future

        Rent rent = new Rent(
                "0123456789",
                "renter@example.com",
                "12345678901",
                birthDate, // Invalid future birth date
                "123456789",
                Rent.Status.Confirmed,
                rental
        );

        Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Birth date must be in the past.");
    }

    @Test
    public void testInvalidDrivingLicenseNumber() throws ServiceException {
        Rental rental = createValidRental();
        Date birthDate = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 18);

        Rent rent = new Rent(
                "0123456789",
                "renter@example.com",
                "12345678901",
                birthDate,
                "12345", // Invalid driving license number
                Rent.Status.Confirmed,
                rental
        );

        Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Driving license number must be 9 digits.");
    }

    @Test
    public void testDefaultConstructorAndSetters() throws ServiceException {
        Rent rent = new Rent();
        rent.setRenterPhoneNumber("0123456789");
        rent.setRenterMail("renter@example.com");
        rent.setNationalRegisterNumber("12345678901");
        rent.setBirthDate(new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 18));
        rent.setDrivingLicenseNumber("123456789");
        rent.setStatus(Rent.Status.Pending);
        rent.setRental(createValidRental());

        assertThat(rent.getRenterPhoneNumber()).isEqualTo("0123456789");
        assertThat(rent.getRenterMail()).isEqualTo("renter@example.com");
        assertThat(rent.getNationalRegisterNumber()).isEqualTo("12345678901");
        assertThat(rent.getStatus()).isEqualTo(Rent.Status.Pending);
        assertThat(rent.getRental()).isNotNull();
    }
}
