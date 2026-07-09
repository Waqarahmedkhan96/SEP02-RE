package model.state;

import model.Vehicle;

public class MaintenanceState implements VehicleState {
    @Override
    public String getStateName() {
        return "maintenance";
    }

    @Override
    public boolean canBeBooked() {
        return false;
    }

    @Override
    public VehicleState rent(Vehicle vehicle) {
        throw new IllegalStateException("Vehicle is under maintenance");
    }

    @Override
    public VehicleState release(Vehicle vehicle) {
        return new AvailableState();
    }
}
