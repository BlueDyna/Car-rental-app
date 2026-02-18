package be.ucll.se.aaron_abbey_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


import be.ucll.se.aaron_abbey_backend.DTO.CarRequest;
import be.ucll.se.aaron_abbey_backend.authentication.JWT.JWTservice;
import be.ucll.se.aaron_abbey_backend.model.Car;
import be.ucll.se.aaron_abbey_backend.model.Rental;
import be.ucll.se.aaron_abbey_backend.model.User;
import be.ucll.se.aaron_abbey_backend.model.User.Role;
import be.ucll.se.aaron_abbey_backend.repository.CarRepo;
import be.ucll.se.aaron_abbey_backend.repository.RentalRepo;
import be.ucll.se.aaron_abbey_backend.repository.UserRepo;
import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CarService {

    @Autowired
    private CarRepo carRepo;

    @Autowired
    private RentalRepo rentalRepo;

    @Autowired
    private JWTservice jwtService;

    @Autowired
    private UserRepo userRepo;


    public List<Car> getAllCars(String token) throws ServiceException {
        try {
            
            Role role = Role.valueOf(jwtService.getRoleFromToken(token));
            Long id = Long.valueOf(jwtService.getIdFromToken(token));

            if (role == Role.Admin) {
                return carRepo.findAll();
            } else if (role == Role.Owner) {
                return getCarsByOwnerId(id);
            } else {
                throw new ServiceException("role", "role.invalid");
            }
        } catch (Exception e) {
            throw new ServiceException("Find all cars", "Error finding all cars: " + e.getMessage());
        }
    }


    public Car findCarById(long id) throws ServiceException {
        return carRepo.findById(id).orElseThrow(() -> new ServiceException("findCar", "Car with id " + id + " not found"));
    }


    public Car addCar(CarRequest car, String email) throws ServiceException {
        Car foundCar = carRepo.findByLicensePlate(car.getLicensePlate());
        if (foundCar != null) {
            throw new ServiceException("addCar", "Car with license plate " + car.getLicensePlate() + " already exists");
        }

        Car newCar = new Car(car.getBrand(), car.getModel(), car.getType(), car.getLicensePlate(), car.getNumberOfSeats(), car.getNumberOfChildSeats(), car.isHaveFoldingRearSeats(), car.isHasTowBar() ,email);
        return carRepo.save(newCar);
    }

    public void deleteCar(long id) throws ServiceException {
        try {
            if (!carRepo.existsById(id)) {
                throw new ServiceException("deleteCar", "Car with id " + id + " not found");
            }

            List<Rental> rentals = rentalRepo.findAll();
            for (Rental rental : rentals) {
                if (rental.getCar().getId() == id) {
                    throw new ServiceException("deleteCar", "Car with id " + id + " is still in use.");
                }
            }
            carRepo.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException("deleteCar", "Car with id " + id + " not found");
        }
        
    }


    public List<Car> getCarsByOwnerId(Long id) throws ServiceException {
        User user = userRepo.findById(id).orElse(null);
        if (user == null) {
            throw new ServiceException("User", "User not found.");
        }

        List<Car> cars = carRepo.findAllByOwnerEmail(user.getEmail());
        return cars;
    }

    
    

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ServiceException.class })
    public Map<String, String> handleValidationExceptions(ServiceException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getField(), ex.getMessage());
        return errors;
    }
}
