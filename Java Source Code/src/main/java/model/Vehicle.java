package model;

import java.io.Serializable;

public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;

    private int vehicleId;
    private String model;
    private String vehicleType;
    private String color;
    private String engine;
    private String plateNo;
    private double priceHour;
    private double deposit;
    private int noOfTire;
    private double lateFee;
    private String requiredLicense;
    private String condition;
    private int noOfSeats;
    private String currentState;

    public Vehicle() {}

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) {this.vehicleType = vehicleType; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getEngine() { return engine; }
    public void setEngine(String engine) { this.engine = engine; }

    public String getPlateNo() { return plateNo; }
    public void setPlateNo(String plateNo) { this.plateNo = plateNo; }

    public double getPriceHour() { return priceHour; }
    public void setPriceHour(double priceHour) { this.priceHour = priceHour; }

    public double getDeposit() { return deposit; }
    public void setDeposit(double deposit) { this.deposit = deposit; }

    public int getNoOfTire() { return noOfTire; }
    public void setNoOfTire(int noOfTire) { this.noOfTire = noOfTire; }

    public double getLateFee() { return lateFee; }
    public void setLateFee(double lateFee) { this.lateFee = lateFee; }

    public String getRequiredLicense() { return requiredLicense; }
    public void setRequiredLicense(String requiredLicense) { this.requiredLicense = requiredLicense; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public int getNoOfSeats() { return noOfSeats; }
    public void setNoOfSeats(int noOfSeats) { this.noOfSeats = noOfSeats; }

    public String getCurrentState() { return currentState; }
    public void setCurrentState(String currentState) { this.currentState = currentState; }
}
