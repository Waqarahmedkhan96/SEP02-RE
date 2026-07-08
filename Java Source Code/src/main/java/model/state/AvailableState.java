package model.state;

import model.Vehicle;

public class AvailableState implements VehicleState {
    @Override
    public String getStateName() {
        return "available";
    }

    @Override
    public boolean canBeBooked() {
        return true;
    }

    @Override
    public VehicleState rent(Vehicle vehicle) {
        return new RentedState();
    }

    @Override
    public VehicleState release(Vehicle vehicle) {
        return this;
    }
}
