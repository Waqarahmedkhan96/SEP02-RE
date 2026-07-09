package model.state;

import model.Vehicle;

public interface VehicleState {
    String getStateName();

    boolean canBeBooked();

    VehicleState rent(Vehicle vehicle);

    VehicleState release(Vehicle vehicle);
}
