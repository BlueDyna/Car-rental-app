package be.ucll.se.aaron_abbey_backend.service;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import be.ucll.se.aaron_abbey_backend.repository.NotificationRepo;
import be.ucll.se.aaron_abbey_backend.repository.RentRepo;
import be.ucll.se.aaron_abbey_backend.repository.UserRepo;
import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;
import be.ucll.se.aaron_abbey_backend.authentication.JWT.JWTservice;
import be.ucll.se.aaron_abbey_backend.mail.MailService;
import be.ucll.se.aaron_abbey_backend.mail.MailService.EmailType;
import be.ucll.se.aaron_abbey_backend.model.Car;
import be.ucll.se.aaron_abbey_backend.model.Notification;
import be.ucll.se.aaron_abbey_backend.model.Notification.NotificationType;
import be.ucll.se.aaron_abbey_backend.model.Rent;
import be.ucll.se.aaron_abbey_backend.model.Rental;
import be.ucll.se.aaron_abbey_backend.model.User;
import be.ucll.se.aaron_abbey_backend.model.User.Role;

@Service
public class NotificationService {


    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private RentRepo rentRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTservice jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;


    

    public List<Notification> getAllNotifications(String token) throws ServiceException {
        try {
            Role role = Role.valueOf(jwtService.getRoleFromToken(token));

            if (role == Role.Admin) {
                return notificationRepo.findAll();
            } else if (role == Role.Owner || role == Role.Renter) {
                return getMyNotifications(jwtService.getIdFromToken(token));
            } else {
                throw new ServiceException("Get all notifications", "Role invalid");
            }
        } catch (Exception e) {
            throw new ServiceException("Find all notifications", "Error finding all notifications: " + e.getMessage());
        }
    }


    private List<Notification> getMyNotifications(Long userId) throws ServiceException {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ServiceException("Notifications", "user not found."));

        List<Rent> rents = rentRepo.findAllByRenterMail(user.getEmail());
        if (rents.isEmpty()) {
            throw new ServiceException("Rent", "No rents for user found.");
        }

        List<Notification> notifications = notificationRepo.findAll();
        // if (notifications == null || notifications.isEmpty()) {
        //     throw new ServiceException("Notification", "No notifications found.");
        // }

        List<Notification> myNotifications = new ArrayList<>();

        for (Rent rent : rents) {
            for (Notification notification : notifications) {
                if (notification.getRentId().equals(rent.getId())) {
                    myNotifications.add(notification);
                }
            }
        }
        return myNotifications;
    }
    
    


    public void confirmNotification(long id, boolean isConfirmed) throws Exception {
        Rent rent = rentRepo.findById(id).orElseThrow(() -> new ServiceException("confirmNotification", "Rent with id " + id + " not found"));


        // add if statement to check if the rent is already confirmed
        if (rent.getStatus() == Rent.Status.Confirmed) {
            throw new ServiceException("confirmNotification", "This Rent is already confirmed");
        }

        if (rent.getStatus() == Rent.Status.Cancelled) {
            throw new ServiceException("confirmNotification", "This Rent has already been cancelled");
        }

        if (isConfirmed == true) {
            sendNotification(rent, NotificationType.CONFIRMATION);
            rent.setStatus(Rent.Status.Confirmed);
            rentRepo.save(rent);
        
            
        } else if (isConfirmed == false) {
            sendNotification(rent, NotificationType.CANCELATION);
            rent.setStatus(Rent.Status.Cancelled);
            rentRepo.save(rent);
        }
        
    }
    

    public Notification findNotificationById(long id) throws ServiceException {
        return notificationRepo.findById(id).orElseThrow(() -> new ServiceException("findNotificationById", "Notification with id " + id + " not found"));
    }


    public Notification saveNotification(Notification notification) {
        return notificationRepo.save(notification);
    }


    public void sendNotification(Rent rent, NotificationType type) {
        try {
            Rental rental = rent.getRental();
            Car car = rental.getCar();

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

            String startDate = formatter.format(rent.getRental().getStartDate());
            String endDate = formatter.format(rent.getRental().getEndDate());
            User carOwner = userService.findUserByEmail(rent.getRental().getCar().getOwnerEmail());
            User renter = userService.findUserByEmail(rent.getRenterMail());

            Notification renterNotification = new Notification("", rent.getId(), renter.getId(), new Date(), type);
            Notification ownerNotification = new Notification("", rent.getId(), carOwner.getId(), new Date(), type);
            String message;
          
            // SEND NOTIFICATION TO OWNER
            message = mailService.sendNotificationMail(carOwner.getEmail(), ownerNotification.getType(),
                    carOwner.getUsername(), renter.getUsername(), car.getBrand(), car.getLicensePlate(), startDate,
                    endDate, EmailType.OWNER_NOTIFICATION);
            ownerNotification.setMessage(message);
            saveNotification(ownerNotification);

            // SEND NOTIFICATION TO RENTER
            message = mailService.sendNotificationMail(renter.getEmail(), renterNotification.getType(),
            carOwner.getUsername(), renter.getUsername(), car.getBrand(), car.getLicensePlate(), startDate,
            endDate, EmailType.RENTER_NOTIFICATION);
            renterNotification.setMessage(message);
            saveNotification(renterNotification);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
