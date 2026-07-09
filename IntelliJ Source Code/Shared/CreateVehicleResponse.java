package shared;

import java.io.Serializable;

public class CreateVehicleResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final String message;
    private final int vehicleId;

    public CreateVehicleResponse(boolean success, String message, int vehicleId) {
        this.success = success;
        this.message = message;
        this.vehicleId = vehicleId;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public int getVehicleId() { return vehicleId; }
}
