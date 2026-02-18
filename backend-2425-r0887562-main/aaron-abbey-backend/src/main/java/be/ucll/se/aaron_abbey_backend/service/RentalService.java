package be.ucll.se.aaron_abbey_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import be.ucll.se.aaron_abbey_backend.DTO.RentalRequest;
import be.ucll.se.aaron_abbey_backend.model.Car;
import be.ucll.se.aaron_abbey_backend.model.Rent;
import be.ucll.se.aaron_abbey_backend.model.Rent.Status;
import be.ucll.se.aaron_abbey_backend.model.Rental;
import be.ucll.se.aaron_abbey_backend.repository.CarRepo;
import be.ucll.se.aaron_abbey_backend.repository.RentRepo;
import be.ucll.se.aaron_abbey_backend.repository.RentalRepo;
import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;

@Service
public class RentalService {

    @Autowired
    private CarRepo carRepo;

    @Autowired
    private RentalRepo rentalRepo;

    @Autowired
    private RentRepo rentRepo;

    
    

    public List<Rental> getAllRentals() throws ServiceException {
        return rentalRepo.findAll();
    }

    public Rental addRental(RentalRequest rental, long carId) throws ServiceException {
        Car car = carRepo.findById(carId).orElseThrow(() -> new ServiceException("createRental","Car with id " + carId + " not found"));


        if (rental.getStartDate().after(rental.getEndDate())) {
            throw new ServiceException("createRental", "Start date must be before end date");
        }

        

        Rental newRental = new Rental(rental.getStartDate(), rental.getEndDate(), rental.getCity(), rental.getStreet(),
                rental.getPostalCode(), rental.getPhoneNumber(), rental.getEmail(), car);
        return rentalRepo.save(newRental);
    }


    
    public Rental getRentalById(long id) throws ServiceException {
        return rentalRepo.findById(id).orElseThrow(() -> new ServiceException("searchRental", "Rental with id " + id + " not found"));
    }

    public void deleteRental(Long id) throws ServiceException {
        try {
            List<Rent> rentsOne = rentRepo.findAll();
            List<Rent> rents = new ArrayList<>();
            for (Rent rent : rentsOne) {
                if (rent.getRental().getId() == id) {
                    rents.add(rent);
                }
            }

            for (Rent rent : rents) {
                if (rent.getStatus() == Status.Pending) {
                    throw new ServiceException("Rental", "Unable to delete rental. There are still rents active");
                }
            }
            rentalRepo.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException("Rental", "Error deleting rental: " + e.getMessage());
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
    

