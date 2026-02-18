package be.ucll.se.aaron_abbey_backend.model;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.util.Set;

public class CarTest {

    private static Validator validator;

    @BeforeAll
    public static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    public void testCarValidData() {
        Car car = new Car(
                "Toyota",
                "Corolla",
                "Sedan",
                "1-ABC-123",
                5,
                2,
                true,
                true,
                "owner@example.com"
        );

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertThat(violations).isEmpty();
    }

    @Test
    public void testCarInvalidLicensePlate() {
        Car car = new Car(
                "Toyota",
                "Corolla",
                "Sedan",
                "INVALID",
                5,
                2,
                true,
                true,
                "owner@example.com"
        );

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("License plate cannot be empty and must be in the format 1-ABC-123.");
    }

    @Test
    public void testCarInvalidEmail() {
        Car car = new Car(
                "Toyota",
                "Corolla",
                "Sedan",
                "1-ABC-123",
                5,
                2,
                true,
                true,
                "invalid-email"
        );

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("mail should be valid");
    }

    @Test
    public void testCarNegativeNumberOfSeats() {
        Car car = new Car(
                "Toyota",
                "Corolla",
                "Sedan",
                "1-ABC-123",
                -1,
                2,
                true,
                true,
                "owner@example.com"
        );

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Number of seats cannot be a negative number.");
    }

    @Test
    public void testCarNegativeChildSeats() {
        Car car = new Car(
                "Toyota",
                "Corolla",
                "Sedan",
                "1-ABC-123",
                5,
                -1,
                true,
                true,
                "owner@example.com"
        );

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Number of child seats cannot be a negative number.");
    }

    @Test
    public void testCarEmptyBrand() {
        Car car = new Car(
                "",
                "Corolla",
                "Sedan",
                "1-ABC-123",
                5,
                2,
                true,
                true,
                "owner@example.com"
        );

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Brand is required.");
    }

    @Test
    public void testCarNullModel() {
        Car car = new Car(
                "Toyota",
                null,
                "Sedan",
                "1-ABC-123",
                5,
                2,
                true,
                true,
                "owner@example.com"
        );

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Model is required.");
    }

    @Test
    public void testCarDefaultConstructor() {
        Car car = new Car("Tesla", "Model S", "Electric", "2-ZXY-456", 4, 1, true, false, "tesla.owner@example.com");

        assertThat(car.getBrand()).isEqualTo("Tesla");
        assertThat(car.getModel()).isEqualTo("Model S");
        assertThat(car.getType()).isEqualTo("Electric");
        assertThat(car.getLicensePlate()).isEqualTo("2-ZXY-456");
        assertThat(car.getNumberOfSeats()).isEqualTo(4);
        assertThat(car.getNumberOfChildSeats()).isEqualTo(1);
        assertThat(car.isHaveFoldingRearSeats()).isTrue();
        assertThat(car.isHasTowBar()).isFalse();
        assertThat(car.getOwnerEmail()).isEqualTo("tesla.owner@example.com");
    }
}
