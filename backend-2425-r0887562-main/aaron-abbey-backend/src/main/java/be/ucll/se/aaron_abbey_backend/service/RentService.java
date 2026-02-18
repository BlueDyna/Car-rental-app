package be.ucll.se.aaron_abbey_backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import be.ucll.se.aaron_abbey_backend.DTO.RentRequest;
import be.ucll.se.aaron_abbey_backend.authentication.JWT.JWTservice;
import be.ucll.se.aaron_abbey_backend.model.Rent;
import be.ucll.se.aaron_abbey_backend.model.Rental;
import be.ucll.se.aaron_abbey_backend.model.User;
import be.ucll.se.aaron_abbey_backend.model.Notification.NotificationType;
import be.ucll.se.aaron_abbey_backend.model.User.Role;
import be.ucll.se.aaron_abbey_backend.repository.RentRepo;
import be.ucll.se.aaron_abbey_backend.repository.RentalRepo;
import be.ucll.se.aaron_abbey_backend.repository.UserRepo;
import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;

@Service
public class RentService {

    @Autowired
    private RentRepo rentRepo;

    @Autowired
    private RentalRepo rentalRepo;


    @Autowired
    private JWTservice jwtService;

   
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private NotificationService notificationService;

    public List<Rent> getAllRents(String token) throws ServiceException {
        try {
            Role role = Role.valueOf(jwtService.getRoleFromToken(token));

            if (role == Role.Admin) {
                return rentRepo.findAll();
            } else if (role == Role.Renter) {
                return getRentsByRenterId(jwtService.getIdFromToken(token));
            } else {
                throw new ServiceException("find All Rents", "Role not valid");
            }
        } catch (Exception e) {
            throw new ServiceException("Find all rents", "Error finding rents: " + e.getMessage());
        }
    }

    public List<Rent>  getRentsByRenterId(Long userId) throws ServiceException {
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            throw new ServiceException("User", "User not found.");
        }

        List<Rent> rents = rentRepo.findAllByRenterMail(user.getEmail());
        return rents;
    }

    public Rent getRentById(long id) throws ServiceException {
        return rentRepo.findById(id).orElseThrow(() -> new ServiceException("findRent", "Rent with id " + id + " not found"));
    }

    public Rent addRent(RentRequest rent, long rentalId) throws Exception {

        Rental rental = rentalRepo.findById(rentalId).orElseThrow(() -> new ServiceException("createRent","Rental with id " + rentalId + " not found"));


        // User owner = userRepo.findByEmail(rental.getCar().getOwnerEmail());
        User renter = userRepo.findByEmail(rent.getRenterMail());
        if (renter == null) {
            throw new ServiceException("createRent", "Renter with email " + rent.getRenterMail() + " not found");
        }

        // check if rent has a confirmmed status already exists for this rental
        List<Rent> rents = rentRepo.findAllByRentalId(rentalId);
        for (Rent r : rents) {
            if (r.getStatus() == Rent.Status.Confirmed) {
                throw new ServiceException("createRent", "There is already a confirmed rent for this rental");
            }
        }
       

        Rent newRent = new Rent(rent.getRenterPhoneNumber(), rent.getRenterMail(), rent.getNationalRegisterNumber(), rent.getBirthDate(), rent.getDrivingLicenseNumber(), Rent.Status.Pending ,rental );
        Rent freshRent = rentRepo.save(newRent);

        notificationService.sendNotification(freshRent, NotificationType.PENDING);

      
        
        return freshRent;
    }

    

    public void cancelRent(long id) throws ServiceException {
       Rent rent = rentRepo.findById(id).orElseThrow(() -> new ServiceException("cancelRent", "Rent with id " + id + " not found"));
        rentRepo.delete(rent);
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
