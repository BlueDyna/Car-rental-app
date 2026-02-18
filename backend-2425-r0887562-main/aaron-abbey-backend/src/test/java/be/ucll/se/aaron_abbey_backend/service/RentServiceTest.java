package be.ucll.se.aaron_abbey_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import be.ucll.se.aaron_abbey_backend.DTO.RentRequest;
import be.ucll.se.aaron_abbey_backend.authentication.JWT.JWTservice;
import be.ucll.se.aaron_abbey_backend.model.Notification.NotificationType;
import be.ucll.se.aaron_abbey_backend.model.Rent;
import be.ucll.se.aaron_abbey_backend.model.Rent.Status;
import be.ucll.se.aaron_abbey_backend.model.Rental;
import be.ucll.se.aaron_abbey_backend.model.User;
import be.ucll.se.aaron_abbey_backend.repository.RentRepo;
import be.ucll.se.aaron_abbey_backend.repository.RentalRepo;
import be.ucll.se.aaron_abbey_backend.repository.UserRepo;
import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class RentServiceTest {

     @Mock
    private RentRepo rentRepo;

    @Mock
    private RentalRepo rentalRepo;

    @Mock
    private JWTservice jwtService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private RentService rentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddRent_Valid() throws Exception {
        // Arrange
        RentRequest rentRequest = new RentRequest();
        rentRequest.setRenterPhoneNumber("123456789");
        rentRequest.setRenterMail("renter@example.com");
        rentRequest.setNationalRegisterNumber("123456789012");
        rentRequest.setBirthDate(Date.valueOf(LocalDate.of(1990, 1, 1)));
        rentRequest.setDrivingLicenseNumber("DL123456");

        Rental rental = new Rental();
        User renter = new User();
        renter.setEmail("renter@example.com");

        when(rentalRepo.findById(1L)).thenReturn(Optional.of(rental));
        when(userRepo.findByEmail("renter@example.com")).thenReturn(renter);
        when(rentRepo.findAllByRentalId(1L)).thenReturn(new ArrayList<>());
        when(rentRepo.save(any(Rent.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Rent result = rentService.addRent(rentRequest, 1L);

        // Assert
        assertEquals(Status.Pending, result.getStatus());
        assertEquals("renter@example.com", result.getRenterMail());
        verify(rentalRepo, times(1)).findById(1L);
        verify(userRepo, times(1)).findByEmail("renter@example.com");
        verify(rentRepo, times(1)).save(any(Rent.class));
        verify(notificationService, times(1)).sendNotification(any(Rent.class), eq(NotificationType.PENDING));
    }

    @Test
    void testAddRent_RentalNotFound() {
        // Arrange
        RentRequest rentRequest = new RentRequest();
        when(rentalRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> rentService.addRent(rentRequest, 1L));
        assertEquals("Rental with id 1 not found", exception.getMessage());
        assertEquals("createRent", exception.getField());
        verify(rentRepo, never()).save(any(Rent.class));
    }

    @Test
    void testAddRent_RenterNotFound() {
        // Arrange
        RentRequest rentRequest = new RentRequest();
        rentRequest.setRenterMail("nonexistent@example.com");

        Rental rental = new Rental();
        when(rentalRepo.findById(1L)).thenReturn(Optional.of(rental));
        when(userRepo.findByEmail("nonexistent@example.com")).thenReturn(null);

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> rentService.addRent(rentRequest, 1L));
        assertEquals("Renter with email nonexistent@example.com not found", exception.getMessage());
        assertEquals("createRent", exception.getField());
        verify(rentRepo, never()).save(any(Rent.class));
    }

    @Test
    void testGetRentById_Valid() throws ServiceException {
        // Arrange
        Rent rent = new Rent();
        rent.setId(1L);
        when(rentRepo.findById(1L)).thenReturn(Optional.of(rent));

        // Act
        Rent result = rentService.getRentById(1L);

        // Assert
        assertEquals(1L, result.getId());
        verify(rentRepo, times(1)).findById(1L);
    }

    @Test
    void testGetRentById_NotFound() {
        // Arrange
        when(rentRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> rentService.getRentById(1L));
        assertEquals("Rent with id 1 not found", exception.getMessage());
        assertEquals("findRent", exception.getField());
    }

    @Test
    void testCancelRent_Valid() throws ServiceException {
        // Arrange
        Rent rent = new Rent();
        rent.setId(1L);
        when(rentRepo.findById(1L)).thenReturn(Optional.of(rent));

        // Act
        rentService.cancelRent(1L);

        // Assert
        verify(rentRepo, times(1)).findById(1L);
        verify(rentRepo, times(1)).delete(rent);
    }

    @Test
    void testCancelRent_NotFound() {
        // Arrange
        when(rentRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> rentService.cancelRent(1L));
        assertEquals("Rent with id 1 not found", exception.getMessage());
        assertEquals("cancelRent", exception.getField());
    }
    
}
