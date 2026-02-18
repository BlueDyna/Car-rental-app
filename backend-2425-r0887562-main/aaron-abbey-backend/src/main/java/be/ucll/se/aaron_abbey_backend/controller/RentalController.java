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

import be.ucll.se.aaron_abbey_backend.DTO.RentalRequest;
import be.ucll.se.aaron_abbey_backend.authentication.JWT.JWTservice;
import be.ucll.se.aaron_abbey_backend.model.Rental;
import be.ucll.se.aaron_abbey_backend.service.RentalService;
import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private JWTservice jwtService;

    @GetMapping("/all")
    public List<Rental> getAllRentals(@RequestHeader(name = "Authorization") String authorization) throws ServiceException {
        String token = authorization.split(" ", 0)[1];
        jwtService.verifyToken(token);
        if (!jwtService.verifyRole(token, "Admin") && !jwtService.verifyRole(token, "Owner") && !jwtService.verifyRole(token, "Renter")) {
            throw new ServiceException("role", "role.invalid");
            
        }
        return rentalService.getAllRentals();
    }

    @PostMapping("/create/{carId}")
    public void addRental(@RequestHeader(name = "Authorization") String authorization, @RequestBody RentalRequest rental, @PathVariable long carId) throws ServiceException {
        String token = authorization.split(" ", 0)[1];
        jwtService.verifyToken(token);
        if (!jwtService.verifyRole(token, "Admin") && !jwtService.verifyRole(token, "Owner")) {
            throw new ServiceException("role", "role.invalid");
        }
        rentalService.addRental(rental, carId);
    }

    

    @DeleteMapping("/cancel/{id}")
    public void deleteRental(@RequestHeader(name = "Authorization") String authorization, @PathVariable long id) throws ServiceException {
        String token = authorization.split(" ", 0)[1];
        jwtService.verifyToken(token);
        if (!jwtService.verifyRole(token, "Admin") && !jwtService.verifyRole(token, "Owner")) {
            throw new ServiceException("role", "role.invalid");
        }
        rentalService.deleteRental(id);
    }

    @GetMapping("/find/{id}")
    public Rental findRentalById(@RequestHeader(name = "Authorization") String authorization, @PathVariable long id) throws ServiceException {
        String token = authorization.split(" ", 0)[1];
        jwtService.verifyToken(token);
        return rentalService.getRentalById(id);
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
