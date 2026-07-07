package mockdata;

import model.Vehicle;

import java.util.List;

public class MockVehicles {

    public static List<Vehicle> getVehicles() {
        return List.of(
                vehicle(1, "Toyota Corolla", "Sedan", "White", "Hybrid", "AB12345", 450, "available"),
                vehicle(2, "VW Golf", "Hatchback", "Black", "Petrol", "CD67890", 520, "available"),
                vehicle(3, "Tesla Model 3", "Electric", "Blue", "Electric", "EF11223", 850, "available"),
                vehicle(4, "Ford Transit", "Van", "Silver", "Diesel", "GH44556", 700, "rented"),
                vehicle(5, "BMW 320i", "Sedan", "Grey", "Petrol", "IJ77889", 780, "available"),
                vehicle(6, "Nissan Qashqai", "SUV", "Red", "Hybrid", "KL99001", 630, "maintenance"),
                vehicle(7, "Mercedes Vito", "Van", "White", "Diesel", "MN22334", 760, "available"),
                vehicle(8, "Audi A4", "Sedan", "Black", "Diesel", "OP55667", 820, "rented"),
                vehicle(9, "Hyundai i30", "Hatchback", "Green", "Petrol", "QR88990", 410, "available"),
                vehicle(10, "Volvo XC60", "SUV", "Blue", "Hybrid", "ST11224", 900, "available")
        );
    }

    public static long countAvailableVehicles() {
        return getVehicles().stream()
                .filter(vehicle -> "available".equalsIgnoreCase(vehicle.getCurrentState()))
                .count();
    }

    private static Vehicle vehicle(int id, String model, String type, String color, String engine,
                                   String plateNo, double priceHour, String state) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(id);
        vehicle.setModel(model);
        vehicle.setVehicleType(type);
        vehicle.setColor(color);
        vehicle.setEngine(engine);
        vehicle.setPlateNo(plateNo);
        vehicle.setPriceHour(priceHour);
        vehicle.setDeposit(1000);
        vehicle.setNoOfTire(4);
        vehicle.setLateFee(75);
        vehicle.setRequiredLicense("B");
        vehicle.setCondition("Good");
        vehicle.setNoOfSeats(5);
        vehicle.setCurrentState(state);
        return vehicle;
    }
}
