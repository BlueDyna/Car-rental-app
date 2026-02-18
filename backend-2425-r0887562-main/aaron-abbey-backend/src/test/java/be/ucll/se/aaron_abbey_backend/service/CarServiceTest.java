package be.ucll.se.aaron_abbey_backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import be.ucll.se.aaron_abbey_backend.DTO.CarRequest;
import be.ucll.se.aaron_abbey_backend.authentication.JWT.JWTservice;
import be.ucll.se.aaron_abbey_backend.model.Car;
import be.ucll.se.aaron_abbey_backend.model.Rental;
import be.ucll.se.aaron_abbey_backend.model.User;
import be.ucll.se.aaron_abbey_backend.repository.CarRepo;
import be.ucll.se.aaron_abbey_backend.repository.RentalRepo;
import be.ucll.se.aaron_abbey_backend.repository.UserRepo;
import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CarServiceTest {

    @Mock
    private CarRepo carRepo;

    @Mock
    private RentalRepo rentalRepo;

    @Mock
    private JWTservice jwtService;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private CarService carService;

    private Car car;
    private CarRequest carRequest;
    private User user;

    @BeforeEach
    void setUp() {
        car = new Car("Audi", "Golf", "SUV", "2-OPO-999", 5, 2, true, true, "owner@example.com");
        car.setId(1L);


        user = new User();
        user.setId(1L);
        user.setEmail("owner@example.com");


        carRequest = new CarRequest();
        carRequest.setBrand("Tesla");
        carRequest.setModel("Model S");
        carRequest.setType("SUV");
        carRequest.setLicensePlate("3-NIN-666");
        carRequest.setNumberOfSeats(5);
        carRequest.setNumberOfChildSeats(2);
        carRequest.setHaveFoldingRearSeats(true);
        carRequest.setHasTowBar(true);
    }

    @Test
    void testGetAllCars_AdminRole() throws ServiceException {
        when(jwtService.getRoleFromToken(anyString())).thenReturn("Admin");
        when(carRepo.findAll()).thenReturn(Arrays.asList(car));

        List<Car> cars = carService.getAllCars("someToken");

        assertNotNull(cars);
        assertEquals(1, cars.size());
        verify(carRepo, times(1)).findAll();
    }

    @Test
    void testGetAllCars_OwnerRole() throws ServiceException {
        when(jwtService.getRoleFromToken(anyString())).thenReturn("Owner");
        when(jwtService.getIdFromToken(anyString())).thenReturn(1L);
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(carRepo.findAllByOwnerEmail(anyString())).thenReturn(Arrays.asList(car));

        List<Car> cars = carService.getAllCars("someToken");

        assertNotNull(cars);
        assertEquals(1, cars.size());
        verify(carRepo, times(1)).findAllByOwnerEmail("owner@example.com");
    }

    @Test
    void testAddCar_Success() throws ServiceException {
        when(carRepo.findByLicensePlate(anyString())).thenReturn(null);
        when(carRepo.save(any(Car.class))).thenReturn(car);


        Car addedCar = carService.addCar(carRequest, "owner@example.com");

        assertNotNull(addedCar);
        assertEquals("Audi", addedCar.getBrand());
        assertEquals("Golf", addedCar.getModel());
        verify(carRepo, times(1)).save(any(Car.class));
    }

    @Test
    void testAddCar_AlreadyExists() {
        when(carRepo.findByLicensePlate(anyString())).thenReturn(car);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            carService.addCar(carRequest, "owner@example.com");
        });

        assertEquals("Car with license plate 3-NIN-666 already exists", exception.getMessage());
        verify(carRepo, never()).save(any(Car.class));
    }

    @Test
    void testFindCarById_Success() throws ServiceException {
        when(carRepo.findById(anyLong())).thenReturn(Optional.of(car));

        Car foundCar = carService.findCarById(1L);

        assertNotNull(foundCar);
        assertEquals("Audi", foundCar.getBrand());
        verify(carRepo, times(1)).findById(1L);
    }

    @Test
    void testFindCarById_NotFound() {
        when(carRepo.findById(anyLong())).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            carService.findCarById(1L);
        });

        assertEquals("Car with id 1 not found", exception.getMessage());
    }

    @Test
    void testDeleteCar_Success() throws ServiceException {
        when(carRepo.existsById(anyLong())).thenReturn(true);
        when(rentalRepo.findAll()).thenReturn(Arrays.asList());

        carService.deleteCar(1L);

        verify(carRepo, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCar_InUse() {
        Rental rental = new Rental();
        rental.setCar(car);
        when(carRepo.existsById(anyLong())).thenReturn(true);
        when(rentalRepo.findAll()).thenReturn(Arrays.asList(rental));

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            carService.deleteCar(1L);
        });

        assertEquals("Car with id 1 not found", exception.getMessage());
        verify(carRepo, never()).deleteById(1L);
    }

    @Test
    void testDeleteCar_NotFound() {
        when(carRepo.existsById(anyLong())).thenReturn(false);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            carService.deleteCar(1L);
        });

        assertEquals("Car with id 1 not found", exception.getMessage());
        verify(carRepo, never()).deleteById(anyLong());
    }

    @Test
    void testGetCarsByOwnerId_Success() throws ServiceException {
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(carRepo.findAllByOwnerEmail(anyString())).thenReturn(Arrays.asList(car));

        List<Car> cars = carService.getCarsByOwnerId(1L);

        assertNotNull(cars);
        assertEquals(1, cars.size());
        verify(carRepo, times(1)).findAllByOwnerEmail("owner@example.com");
    }

    @Test
    void testGetCarsByOwnerId_UserNotFound() {
        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            carService.getCarsByOwnerId(1L);
        });

        assertEquals("User not found.", exception.getMessage());
        verify(carRepo, never()).findAllByOwnerEmail(anyString());
    }
}
