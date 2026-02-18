package be.ucll.se.aaron_abbey_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.ucll.se.aaron_abbey_backend.DTO.CarRequest;
import be.ucll.se.aaron_abbey_backend.authentication.JWT.JWTservice;
import be.ucll.se.aaron_abbey_backend.model.Car;
import be.ucll.se.aaron_abbey_backend.service.CarService;
import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;

@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    public JWTservice jwtService;

    @GetMapping("/all")
    public List<Car> getAllCars(@RequestHeader(name = "Authorization") String authorization) throws ServiceException {
        String token = authorization.split(" ", 0)[1];
        jwtService.verifyToken(token);
        if (!jwtService.verifyRole(token, "Admin") && !jwtService.verifyRole(token, "Owner")) {
            throw new ServiceException("role", "role.invalid");
        }
        return carService.getAllCars(token);
    }

    @PostMapping("/add")
    public Car addCar(@RequestHeader(name = "Authorization") String authorization,
         @RequestBody CarRequest car) throws ServiceException {
        String token = authorization.split(" ", 0)[1];
        jwtService.verifyToken(token);
        if (!jwtService.verifyRole(token, "Admin") && !jwtService.verifyRole(token, "Owner")) {
            throw new ServiceException("role", "role.invalid");
        }
       String mail = jwtService.getSubjectFromToken(token);
       return carService.addCar(car, mail);
    }

    @GetMapping("/find/{id}")
    public Car findCarById(@RequestHeader(name = "Authorization") String authorization,@PathVariable long id) throws ServiceException {
        String token = authorization.split(" ", 0)[1];
        jwtService.verifyToken(token);
        if (!jwtService.verifyRole(token, "Admin") && !jwtService.verifyRole(token, "Owner")) {
            throw new ServiceException("role", "role.invalid");
        }
        return carService.findCarById(id);
    }
    
    @DeleteMapping("/delete/{id}")
    public void deleteCar(@RequestHeader(name = "Authorization") String authorization, @PathVariable long id) throws ServiceException {
        String token = authorization.split(" ", 0)[1];
        jwtService.verifyToken(token);
        if (!jwtService.verifyRole(token, "Admin") && !jwtService.verifyRole(token, "Owner")) {
            throw new ServiceException("role", "role.invalid");
        }
        carService.deleteCar(id);
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
