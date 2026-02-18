package be.ucll.se.aaron_abbey_backend.service;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import be.ucll.se.aaron_abbey_backend.DTO.UserRequest;
import be.ucll.se.aaron_abbey_backend.authentication.JWT.JWTservice;
import be.ucll.se.aaron_abbey_backend.model.User;
import be.ucll.se.aaron_abbey_backend.model.User.Role;
import be.ucll.se.aaron_abbey_backend.repository.UserRepo;
import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {
     @Mock
    private UserRepo userRepository;

    @Mock
    private JWTservice jwtService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsers() {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(new User("JohnDoe", "password123", "johndoe@example.com", Role.Renter));
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getUsers();

        // Assert
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        // Arrange
        User user = new User("JohnDoe", "password123", "johndoe@example.com", Role.Renter);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("johndoe@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        User result = userService.getUserById(1L);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testAddUser_Success() throws ServiceException {
        // Arrange
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("aaron@test.com");
        userRequest.setPassword("password123");
        userRequest.setRole("Renter");
        userRequest.setUsername("Aaron");

        User user = new User("JaneDoe", "securePassword", "janedoe@example.com", Role.Renter);
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.addUser(userRequest);

        // Assert
        assertNotNull(result);
        assertEquals("janedoe@example.com", result.getEmail());
        verify(userRepository, times(1)).findByEmail(userRequest.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testAddUser_EmailExists() {
        // Arrange
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("john@doe.com");
        userRequest.setPassword("password123");
        userRequest.setRole("Owner");
        userRequest.setUsername("John");
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(new User());

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.addUser(userRequest);
        });
        assertEquals("Error adding user: email.already.exists", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(userRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    // @Test
    // void testAuthenticateUser_Success() throws ServiceException {
    //     // Arrange
    //     User user = new User("JohnDoe", "encodedPassword", "johndoe@example.com", Role.Renter);
    //     when(userRepository.findByEmail("johndoe@example.com")).thenReturn(user);
    //     when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
    //     when(jwtService.createToken("johndoe@example.com", "Renter", user.getId())).thenReturn("jwtToken");

    //     // Act
    //     String token = userService.authenticateUser(new User("JohnDoe", "password123", "johndoe@example.com", Role.Renter));

    //     // Assert
    //     assertNotNull(token);
    //     assertEquals("jwtToken", token);
    //     verify(userRepository, times(1)).findByEmail("johndoe@example.com");
    // }

    @Test
    void testAuthenticateUser_InvalidCredentials() {
        // Arrange
        User user = new User("JohnDoe", "encodedPassword", "johndoe@example.com", Role.Renter);
        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(user);
        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.authenticateUser(new User(null, "wrongPassword", "johndoe@example.com", null));
        });
        assertEquals("Invalid credentials", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("johndoe@example.com");
    }

    @Test
    void testDeleteUser_Success() throws ServiceException {
        // Arrange
        User user = new User("JohnDoe", "password123", "johndoe@example.com", Role.Renter);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User result = userService.deleteUser(1L);

        // Assert
        assertNotNull(result);
        assertEquals("johndoe@example.com", result.getEmail());
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.deleteUser(1L);
        });
        assertEquals("User with id 1 does not exist", exception.getMessage());
        verify(userRepository, never()).deleteById(anyLong());
    }
    
}
