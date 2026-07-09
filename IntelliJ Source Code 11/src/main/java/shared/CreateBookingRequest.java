package shared;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CreateBookingRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private int customerId;
    private int vehicleId;
    private int employeeId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public CreateBookingRequest(int customerId, int vehicleId, int employeeId,
                                LocalDateTime startDate, LocalDateTime endDate) {
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getCustomerId() { return customerId; }
    public int getVehicleId() { return vehicleId; }
    public int getEmployeeId() { return employeeId; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
}
