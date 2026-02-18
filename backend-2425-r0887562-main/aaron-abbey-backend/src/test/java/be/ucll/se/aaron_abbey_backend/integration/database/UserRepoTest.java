package be.ucll.se.aaron_abbey_backend.integration.database;

import be.ucll.se.aaron_abbey_backend.model.User;
import be.ucll.se.aaron_abbey_backend.repository.UserRepo;
import jakarta.transaction.Transactional;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class UserRepoTest {

   
    @Autowired
    private UserRepo userRepo;

    @Test
    void testFindByEmail_UserExists() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("Test User");
        user.setPassword("password123");
        user.setRole(User.Role.Renter);
        userRepo.save(user);

        // Act
        User foundUser = userRepo.findByEmail("test@example.com");

        // Assert
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
        assertThat(foundUser.getUsername()).isEqualTo("Test User");
    }

    @Test
    void testFindByEmail_UserNotExists() {
        // Act
        User foundUser = userRepo.findByEmail("nonexistent@example.com");

        // Assert
        assertThat(foundUser).isNull();
    }

    @Test
    void testSaveAndRetrieveUser() {
        // Arrange
        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setUsername("John Doe");
        user.setPassword("securePassword");
        user.setRole(User.Role.Renter);
        userRepo.save(user);

        // Act
        Optional<User> retrievedUser = userRepo.findById(user.getId());

        // Assert
        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getEmail()).isEqualTo("john.doe@example.com");
        assertThat(retrievedUser.get().getUsername()).isEqualTo("John Doe");
    }
}
