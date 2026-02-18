package be.ucll.se.aaron_abbey_backend.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_seq")
    @SequenceGenerator(name = "car_seq", sequenceName = "cars_id_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message = "Brand is required.")
    @NotNull(message = "Brand is required.")
    private String brand;

    @NotBlank(message = "Model is required.")
    @NotNull(message = "Model is required.")
    private String model;

    @NotBlank(message = "Type is required.")
    @NotNull(message = "Type is required.")
    private String type;

    @Pattern(regexp = "[0-9]{1}-[A-Z]{3}-[0-9]{3}", message = "License plate cannot be empty and must be in the format 1-ABC-123.")
    @NotNull(message = "License plate is required.")
    private String licensePlate;

    @NotNull(message = "Number of seats is required.")
    @PositiveOrZero(message = "Number of seats cannot be a negative number.")
    private int numberOfSeats;

    @PositiveOrZero(message = "Number of child seats cannot be a negative number.")
    private int numberOfChildSeats;

    private boolean haveFoldingRearSeats;

    private boolean hasTowBar;

    @Email(message = "mail should be valid")
    @NotNull(message = "Owner email is required.")
    private String ownerEmail;

    public Car() {
    }


    public Car(String brand, String model, String type, String licensePlate, int numberOfSeats, int numberOfChildSeats, boolean haveFoldingRearSeats, boolean hasTowBar,  String ownerEmail) {
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.licensePlate = licensePlate;
        this.numberOfSeats = numberOfSeats;
        this.numberOfChildSeats = numberOfChildSeats;
        this.haveFoldingRearSeats = haveFoldingRearSeats;
        this.hasTowBar = hasTowBar;
        this.ownerEmail = ownerEmail;
    }

    public Long getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getType() {
        return type;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    
    public String getLicensePlate() {
        return licensePlate;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public int getNumberOfChildSeats() {
        return numberOfChildSeats;
    }

    public boolean isHaveFoldingRearSeats() {
        return haveFoldingRearSeats;
    }


    public boolean isHasTowBar() {
        return hasTowBar;
    }


    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public void setNumberOfChildSeats(int numberOfChildSeats) {
        this.numberOfChildSeats = numberOfChildSeats;
    }

    public void setHaveFoldingRearSeats(boolean haveFoldingRearSeats) {
        this.haveFoldingRearSeats = haveFoldingRearSeats;
    }

    public void setHasTowBar(boolean hasTowBar) {
        this.hasTowBar = hasTowBar;
    }


   public void setId(Long id) {
       this.id = id;
   }

    
}