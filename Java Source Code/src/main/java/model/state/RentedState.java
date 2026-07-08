package model.state;

import model.Vehicle;

public class RentedState implements VehicleState {
    @Override
    public String getStateName() {
        return "rented";
    }

    @Override
    public boolean canBeBooked() {
        return false;
    }

    @Override
    public VehicleState rent(Vehicle vehicle) {
        throw new IllegalStateException("Vehicle is already rented");
    }

    @Override
    public VehicleState release(Vehicle vehicle) {
        return new AvailableState();
    }
}
