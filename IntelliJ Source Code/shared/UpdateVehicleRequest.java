package shared;

import java.io.Serializable;

import model.Vehicle;

public class UpdateVehicleRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Vehicle vehicle;

    public UpdateVehicleRequest(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
