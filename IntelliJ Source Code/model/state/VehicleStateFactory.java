package model.state;

public class VehicleStateFactory {
    public static VehicleState fromState(String state) {
        if (state == null || state.isBlank()) {
            return new AvailableState();
        }

        return switch (state.trim().toLowerCase()) {
            case "available" -> new AvailableState();
            case "rented" -> new RentedState();
            case "maintenance" -> new MaintenanceState();
            default -> throw new IllegalArgumentException("Unknown vehicle state: " + state);
        };
    }
}
