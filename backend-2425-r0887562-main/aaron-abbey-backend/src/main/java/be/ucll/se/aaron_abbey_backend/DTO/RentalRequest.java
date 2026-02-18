package be.ucll.se.aaron_abbey_backend.DTO;

import java.util.Date;

public class RentalRequest {
 
    
    private Date startDate;
    
    private Date endDate;
    private String city;

    private String street;


    private int postalCode;

    private String phoneNumber;

    private String email;

    public RentalRequest() {
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

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
