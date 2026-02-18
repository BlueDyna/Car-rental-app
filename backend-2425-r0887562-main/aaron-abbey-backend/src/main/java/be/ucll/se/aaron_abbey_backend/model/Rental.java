package be.ucll.se.aaron_abbey_backend.model;


import java.util.Date;

import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rental_seq")
    @SequenceGenerator(name = "rental_seq", sequenceName = "rentals_id_seq", allocationSize = 1)
    private Long id;
    
    @NotNull
    @FutureOrPresent
    private Date startDate;

    @NotNull
    @Future
    private Date endDate;

    @NotBlank(message = "City is required.")
    private String city;

    @NotBlank(message = "Street is required.")
    private String street;

    @Min(value = 1000, message = "Postal code must be at least 1000.")
    @Max(value = 9999, message = "Postal code must be at most 9999.")
    private int postalCode;

    @Pattern(regexp = "0[0-9]{9}", message = "Phone number must start with 0 and be followed by 9 digits.")
    @NotNull(message = "Phone number is required.")
    private String phoneNumber;

    @Email(message = "Email must be valid.")
    @NotNull(message = "Email is required.")
    private String email;


    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    public Rental() {
    }

    public Rental(Date startDate, Date endDate, String city, String street, int postalCode, String phoneNumber, String email, Car car) throws ServiceException {
        this.startDate = startDate;
        setEndDate(endDate);
        this.city = city;
        this.street = street;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.car = car;
    }


    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }


    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public Car getCar() {
        return car;
    }

   public Long getId() {
       return id;
   }

    public void setId(Long id) {
         this.id = id;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEndDate(Date endDate) throws ServiceException {
        if (endDate.before(startDate)) {
            throw new ServiceException("endDate","End date must be after start date.");
        }
        this.endDate = endDate;
    }


    public void setCar(Car car) {
        this.car = car;
    }
}


