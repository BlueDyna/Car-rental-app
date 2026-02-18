package be.ucll.se.aaron_abbey_backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import be.ucll.se.aaron_abbey_backend.DTO.RentRequest;
import be.ucll.se.aaron_abbey_backend.authentication.JWT.JWTservice;
import be.ucll.se.aaron_abbey_backend.model.Rent;
import be.ucll.se.aaron_abbey_backend.service.RentService;
import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;

@RestController
@RequestMapping("/rents")
public class RentController {

    @Autowired
    private RentService rentService;

    @Autowired
    private JWTservice jwtService;


    @GetMapping("/all")
    public List<Rent> getAllRents(@RequestHeader(name = "Authorization") String authorization) throws ServiceException {
        String token = authorization.split(" ", 0)[1];
        jwtService.verifyToken(token);
        if (!jwtService.verifyRole(token, "Admin") && !jwtService.verifyRole(token, "Renter")) {
            throw new ServiceException("role", "role.invalid");
        }
        return rentService.getAllRents(token);
    }

    @PostMapping("/create/{rentalId}")
    public Rent addRent(@RequestHeader(name = "Authorization") String authorization, @RequestBody RentRequest rent, @PathVariable long rentalId) throws Exception, ServiceException {
        String token = authorization.split(" ", 0)[1];
        jwtService.verifyToken(token);
        if (!jwtService.verifyRole(token, "Admin") && !jwtService.verifyRole(token, "Renter")) {
            throw new ServiceException("role", "role.invalid");
        }
        return rentService.addRent(rent, rentalId);
    }

  
    @DeleteMapping("/cancel/{id}")
    public void cancelRent(@RequestHeader(name = "Authorization") String authorization, @PathVariable long id) throws ServiceException {
        String token = authorization.split(" ", 0)[1];
        jwtService.verifyToken(token);
        if (!jwtService.verifyRole(token, "Admin") && !jwtService.verifyRole(token, "Renter")) {
            throw new ServiceException("role", "role.invalid");
        }
        rentService.cancelRent(id);
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
