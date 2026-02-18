package be.ucll.se.aaron_abbey_backend.DTO;

import java.util.Date;



public class RentRequest {

    private String renterPhoneNumber;

    private String renterMail;

    private String nationalRegisterNumber;

    // driver must be at least 18 years old
    private Date birthDate;

    private String drivingLicenseNumber;

    


    public RentRequest() {
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
    
}
