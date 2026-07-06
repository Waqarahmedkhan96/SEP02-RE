package dao;

import java.time.LocalDateTime;
import java.util.List;

import model.Vehicle;

public interface VehicleDAO {
List<Vehicle> getAllVehicles();

List<Vehicle> filterVehicles(String color, String vehicleType, String status, double maxPrice);

Vehicle getVehicleById(int id);

 List<Vehicle> getAvailableVehicles( LocalDateTime startDate, LocalDateTime endDate);
}
