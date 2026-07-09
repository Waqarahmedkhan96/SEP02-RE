package shared;

import java.io.Serializable;
import java.util.List;

import model.Vehicle;

public class CheckAvailabilityResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Vehicle> availableVehicles;

    public CheckAvailabilityResponse(List<Vehicle> availableVehicles) {
        this.availableVehicles = availableVehicles;
    }

    public List<Vehicle> getAvailableVehicles() {
        return availableVehicles;
    }
}
