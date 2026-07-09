package shared;

import java.io.Serializable;

import model.Vehicle;

public class CreateVehicleRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Vehicle vehicle;

    public CreateVehicleRequest(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
