package be.ucll.se.aaron_abbey_backend.authentication.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.se.aaron_abbey_backend.DTO.LoginRequest;
import be.ucll.se.aaron_abbey_backend.DTO.UserRequest;
import be.ucll.se.aaron_abbey_backend.model.User;
import be.ucll.se.aaron_abbey_backend.service.UserService;
import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {

     private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    Map<String, Object> login(@RequestBody LoginRequest u) throws ServiceException {
        Map<String, Object> result = new HashMap<>();
        User userR = LoginRequest.toUser(u);

        User user = userService.findUserByEmail(userR.getEmail());
        String token = userService.authenticateUser(userR);

        result.put("user", user);

        result.put("token", token);

        return result;
    }

    @PostMapping("/register")
    Map<String, Object> register(@RequestBody UserRequest u) throws ServiceException {
        Map<String, Object> result = new HashMap<>();
        User user = userService.addUser(u);


        result.put("user", user);

        result.put("token", "go to login.");

        return result;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({ ServiceException.class })
    public Map<String, String> handleValidationExceptions(ServiceException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getField(), ex.getMessage());
        return errors;
    }
    
}
