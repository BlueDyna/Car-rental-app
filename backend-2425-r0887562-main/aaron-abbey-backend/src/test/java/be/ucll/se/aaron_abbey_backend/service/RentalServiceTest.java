package be.ucll.se.aaron_abbey_backend.service;

import be.ucll.se.aaron_abbey_backend.DTO.RentalRequest;
import be.ucll.se.aaron_abbey_backend.model.Car;
import be.ucll.se.aaron_abbey_backend.model.Rent;
import be.ucll.se.aaron_abbey_backend.model.Rental;
import be.ucll.se.aaron_abbey_backend.repository.CarRepo;
import be.ucll.se.aaron_abbey_backend.repository.RentRepo;
import be.ucll.se.aaron_abbey_backend.repository.RentalRepo;
import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class RentalServiceTest {

    @Mock
    private CarRepo carRepo;

    @Mock
    private RentalRepo rentalRepo;

    @Mock
    private RentRepo rentRepo;

    @InjectMocks
    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRentals() throws ServiceException {
        // Arrange
        List<Rental> rentals = new ArrayList<>();
        rentals.add(new Rental());
        when(rentalRepo.findAll()).thenReturn(rentals);

        // Act
        List<Rental> result = rentalService.getAllRentals();

        // Assert
        assertEquals(1, result.size());
        verify(rentalRepo, times(1)).findAll();
    }

    @Test
    void testAddRental() throws ServiceException {
        // Arrange
        RentalRequest rentalRequest = new RentalRequest();
        rentalRequest.setStartDate(java.sql.Date.valueOf(LocalDate.of(2025, 1, 1)));
        rentalRequest.setEndDate(java.sql.Date.valueOf(LocalDate.of(2025, 1, 5)));
        rentalRequest.setCity("Leuven");
        rentalRequest.setStreet("Main Street");
        rentalRequest.setPostalCode(3000);
        rentalRequest.setPhoneNumber("123456789");
        rentalRequest.setEmail("test@example.com");

        Car car = new Car();
        when(carRepo.findById(1L)).thenReturn(Optional.of(car));

        Rental rental = new Rental(rentalRequest.getStartDate(), rentalRequest.getEndDate(), rentalRequest.getCity(),
                rentalRequest.getStreet(), rentalRequest.getPostalCode(), rentalRequest.getPhoneNumber(),
                rentalRequest.getEmail(), car);
        when(rentalRepo.save(any(Rental.class))).thenReturn(rental);

        // Act
        Rental result = rentalService.addRental(rentalRequest, 1L);

        // Assert
        assertEquals(rentalRequest.getCity(), result.getCity());
        assertEquals(rentalRequest.getStreet(), result.getStreet());
        assertEquals(rentalRequest.getPostalCode(), result.getPostalCode());    
        assertEquals(rentalRequest.getPhoneNumber(), result.getPhoneNumber());
        verify(carRepo, times(1)).findById(1L);
        verify(rentalRepo, times(1)).save(any(Rental.class));
    }

    @Test
    void testAddRental_CarNotFound() {
        // Arrange
        RentalRequest rentalRequest = new RentalRequest();
        when(carRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            rentalService.addRental(rentalRequest, 1L);
        });
        assertEquals("Car with id 1 not found", exception.getMessage());
    }

    @Test
    void testGetRentalById() throws ServiceException {
        // Arrange
        Rental rental = new Rental();
        when(rentalRepo.findById(1L)).thenReturn(Optional.of(rental));

        // Act
        Rental result = rentalService.getRentalById(1L);

        // Assert
        assertNotNull(result);
        verify(rentalRepo, times(1)).findById(1L);
    }

    @Test
    void testGetRentalById_NotFound() {
        // Arrange
        when(rentalRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            rentalService.getRentalById(1L);
        });
        assertEquals("Rental with id 1 not found", exception.getMessage());
    }

    @Test
    void testDeleteRental() throws ServiceException {
        // Arrange
        List<Rent> rents = new ArrayList<>();
        when(rentRepo.findAll()).thenReturn(rents);

        // Act
        rentalService.deleteRental(1L);

        // Assert
        verify(rentalRepo, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteRental_WithActiveRent() {
        // Arrange
        Rental rental = new Rental();
        rental.setId(1L);
        Rent activeRent = new Rent();
        activeRent.setRental(rental);
        activeRent.setStatus(Rent.Status.Pending); // Status should be Active to match the service logic
        List<Rent> rents = new ArrayList<>();
        rents.add(activeRent);

        when(rentRepo.findAll()).thenReturn(rents);

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            rentalService.deleteRental(1L);
        });

        assertEquals("Error deleting rental: Unable to delete rental. There are still rents active", exception.getMessage());
        verify(rentalRepo, never()).deleteById(anyLong());
    }



    @Test
    void testAddRental_InvalidRental() {
        // Arrange
        RentalRequest rentalRequest = new RentalRequest();
        rentalRequest.setStartDate(java.sql.Date.valueOf(LocalDate.of(2024, 2, 1)));
        rentalRequest.setEndDate(java.sql.Date.valueOf(LocalDate.of(2024, 1, 5)));
        rentalRequest.setCity("Leuven");
        rentalRequest.setStreet("Main Street");
        rentalRequest.setPostalCode(3000);
        rentalRequest.setPhoneNumber("123456789");
        rentalRequest.setEmail("test@example.com");

        // Simulate car not found
        Car car = new Car();
        when(carRepo.findById(1L)).thenReturn(Optional.of(car));


        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            rentalService.addRental(rentalRequest, 1L);
        });

        assertEquals("Start date must be before end date", exception.getMessage());
        assertEquals("createRental", exception.getField());

        // Verify that no rental was saved
        verify(carRepo, times(1)).findById(1L);
        verify(rentalRepo, never()).save(any(Rental.class));
    }

}
