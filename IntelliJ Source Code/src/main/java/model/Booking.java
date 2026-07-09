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
}
