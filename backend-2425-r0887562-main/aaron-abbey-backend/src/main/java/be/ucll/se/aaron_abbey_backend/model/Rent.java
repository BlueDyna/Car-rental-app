package be.ucll.se.aaron_abbey_backend.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "rents")
public class Rent {

    @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rent_seq")
    @SequenceGenerator(name = "rent_seq", sequenceName = "rents_id_seq", allocationSize = 1)
    private Long id;
    
    @Pattern(regexp = "0[0-9]{9}", message = "Phone number must start with 0 and be followed by 9 digits.")
    private String renterPhoneNumber;

    @Email(message = "Email must be valid.")
    private String renterMail;

    @Pattern(regexp = "[0-9]{11}", message = "National register number must be 11 digits.")
    private String nationalRegisterNumber;

    @Past(message = "Birth date must be in the past.")
    private Date birthDate;

    @Pattern(regexp = "[0-9]{9}", message = "Driving license number must be 9 digits.")
    private String drivingLicenseNumber;

    private Status status;  

    @OneToMany(mappedBy = "rentId", fetch = FetchType.EAGER)
    private List<Notification> notifications;
    
    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    public Rent() {
    }

    public Rent(String renterPhoneNumber, String renterMail, String nationalRegisterNumber, Date birthDate, String drivingLicenseNumber, Status status ,Rental rental) {
        this.renterPhoneNumber = renterPhoneNumber;
        this.renterMail = renterMail;
        this.nationalRegisterNumber = nationalRegisterNumber;
        this.birthDate = birthDate;
        this.drivingLicenseNumber = drivingLicenseNumber;
        this.rental = rental;
        this.status = status;
    }


    public Date getBirthDate() {
        return birthDate;
    }

    public String getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

    public String getNationalRegisterNumber() {
        return nationalRegisterNumber;
    }

    public String getRenterMail() {
        return renterMail;
    }

    public String getRenterPhoneNumber() {
        return renterPhoneNumber;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setDrivingLicenseNumber(String drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    public void setNationalRegisterNumber(String nationalRegisterNumber) {
        this.nationalRegisterNumber = nationalRegisterNumber;
    }

    public void setRenterMail(String renterMail) {
        this.renterMail = renterMail;
    }

    public void setRenterPhoneNumber(String renterPhoneNumber) {
        this.renterPhoneNumber = renterPhoneNumber;
    }

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public Long getId() {
        return id;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public enum Status {
        Confirmed, // 0
        Cancelled, // 2
        Pending
    }

    public void setId(Long id) {
        this.id = id;
    }
}
