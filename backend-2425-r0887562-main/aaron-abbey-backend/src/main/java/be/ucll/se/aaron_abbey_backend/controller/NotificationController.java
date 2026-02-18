package be.ucll.se.aaron_abbey_backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.se.aaron_abbey_backend.authentication.JWT.JWTservice;
import be.ucll.se.aaron_abbey_backend.model.Notification;
import be.ucll.se.aaron_abbey_backend.service.NotificationService;
import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;


@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private JWTservice jwtService;


    @GetMapping("/all")
    public List<Notification> getNotifications(@RequestHeader(name = "Authorization") String authorization) throws ServiceException {
        String token = authorization.split(" ", 0)[1];
        return notificationService.getAllNotifications(token);
    }

    @PutMapping("search/{id}/confirm/{isConfirmed}")
    public void confirmNotification(@RequestHeader(name = "Authorization") String authorization, @PathVariable("id") long notificationId, @PathVariable boolean isConfirmed ) throws Exception {
        String token = authorization.split(" ", 0)[1];
        jwtService.verifyToken(token);
        if (!jwtService.verifyRole(token, "Admin") && !jwtService.verifyRole(token, "Owner")) {
            throw new ServiceException("role", "role.invalid");
        }
        notificationService.confirmNotification(notificationId, isConfirmed);
    }
    
    @GetMapping("/find/{id}")
    public Notification getNotificationById(@RequestHeader(name = "Authorization") String authorization, @PathVariable long id) throws ServiceException {
        String token = authorization.split(" ", 0)[1];
        jwtService.verifyToken(token);
        return notificationService.findNotificationById(id);
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
