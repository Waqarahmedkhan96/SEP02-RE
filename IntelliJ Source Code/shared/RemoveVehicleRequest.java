package shared;

import java.io.Serializable;

public class RemoveVehicleRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int vehicleId;

    public RemoveVehicleRequest(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getVehicleId() {
        return vehicleId;
    }
}
