package be.ucll.se.aaron_abbey_backend.DTO;

public class CarRequest {
    private String brand;

    private String model;

    private String type;

    private String licensePlate;

    private int numberOfSeats;

    private int numberOfChildSeats;

    private boolean haveFoldingRearSeats;

    private boolean hasTowBar;

   private String ownerEmail;

    public CarRequest() {
    }


    public String getBrand() {
        return brand;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public int getNumberOfChildSeats() {
        return numberOfChildSeats;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public String getModel() {
        return model;
    }

    public String getType() {
        return type;
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

    public void setHasTowBar(boolean hasTowBar) {
        this.hasTowBar = hasTowBar;
    }

    public void setHaveFoldingRearSeats(boolean haveFoldingRearSeats) {
        this.haveFoldingRearSeats = haveFoldingRearSeats;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setNumberOfChildSeats(int numberOfChildSeats) {
        this.numberOfChildSeats = numberOfChildSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }
    public void setType(String type) {
        this.type = type;
    }
   
   
    
}
