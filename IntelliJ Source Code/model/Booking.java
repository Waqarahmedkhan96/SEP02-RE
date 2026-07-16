package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    private int bookingId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime actualReturnDate;
    private String bookingStatus;
    private int customerId;
    private int vehicleId;
    private int employeeId;
    private double priceHour;
    private double bookedHours;
    private double actualHours;
    private double lateHours;
    private double totalHours;
    private double totalPrice;

    public Booking() {}

    public Booking(LocalDateTime startDate, LocalDateTime endDate, String bookingStatus,
                   int customerId, int vehicleId, int employeeId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.bookingStatus = bookingStatus;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.employeeId = employeeId;
    }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public LocalDateTime getActualReturnDate() { return actualReturnDate; }
    public void setActualReturnDate(LocalDateTime actualReturnDate) { this.actualReturnDate = actualReturnDate; }

    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public double getPriceHour() { return priceHour; }
    public void setPriceHour(double priceHour) { this.priceHour = priceHour; }

    public double getBookedHours() { return bookedHours; }
    public void setBookedHours(double bookedHours) { this.bookedHours = bookedHours; }

    public double getActualHours() { return actualHours; }
    public void setActualHours(double actualHours) { this.actualHours = actualHours; }

    public double getLateHours() { return lateHours; }
    public void setLateHours(double lateHours) { this.lateHours = lateHours; }

    public double getTotalHours() { return totalHours; }
    public void setTotalHours(double totalHours) { this.totalHours = totalHours; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}
