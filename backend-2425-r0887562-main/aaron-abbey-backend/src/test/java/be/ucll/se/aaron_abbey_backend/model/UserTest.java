package be.ucll.se.aaron_abbey_backend.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    private static Validator validator;

    @BeforeAll
    public static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidUser() {
        User user = new User(
                "validUsername",
                "strongPassword",
                "user@example.com",
                User.Role.Admin
        );

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isEmpty();
    }

    @Test
    public void testInvalidUsername() {
        User user = new User(
                "", // Invalid username (blank)
                "strongPassword",
                "user@example.com",
                User.Role.Admin
        );

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Username is required.");
    }

    @Test
    public void testInvalidPassword() {
        User user = new User(
                "validUsername",
                "", // Invalid password (blank)
                "user@example.com",
                User.Role.Admin
        );

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Password is required.");
    }

    @Test
    public void testInvalidEmail() {
        User user = new User(
                "validUsername",
                "strongPassword",
                "invalid-email", // Invalid email format
                User.Role.Admin
        );

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Email is required.");
    }

    @Test
    public void testNullRole() {
        User user = new User(
                "validUsername",
                "strongPassword",
                "user@example.com",
                null // Null role
        );

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Role is required.");
    }

    @Test
    public void testDefaultConstructorAndSetters() {
        User user = new User();
        user.setUsername("newUsername");
        user.setPassword("newPassword");
        user.setEmail("newuser@example.com");
        user.setRole(User.Role.Renter);
        user.setId(1L);

        assertThat(user.getUsername()).isEqualTo("newUsername");
        assertThat(user.getPassword()).isEqualTo("newPassword");
        assertThat(user.getEmail()).isEqualTo("newuser@example.com");
        assertThat(user.getRole()).isEqualTo(User.Role.Renter);
        assertThat(user.getId()).isEqualTo(1L);
    }
}
