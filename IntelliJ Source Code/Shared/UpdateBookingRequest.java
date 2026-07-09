package shared;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UpdateBookingRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int bookingId;
    private final int vehicleId;
    private final int employeeId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final String bookingStatus;

    public UpdateBookingRequest(int bookingId, int vehicleId, int employeeId,
                                LocalDateTime startDate, LocalDateTime endDate,
                                String bookingStatus) {
        this.bookingId = bookingId;
        this.vehicleId = vehicleId;
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bookingStatus = bookingStatus;
    }

    public int getBookingId() { return bookingId; }
    public int getVehicleId() { return vehicleId; }
    public int getEmployeeId() { return employeeId; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public String getBookingStatus() { return bookingStatus; }
}
