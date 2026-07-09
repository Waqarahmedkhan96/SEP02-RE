package dao;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import model.Vehicle;

public interface VehicleDAO {
List<Vehicle> getAllVehicles();

int create(Vehicle vehicle) throws SQLException;

void update(Vehicle vehicle) throws SQLException;

void remove(int vehicleId) throws SQLException;

List<Vehicle> filterVehicles(String color, String vehicleType, String status, double maxPrice);

Vehicle getVehicleById(int id);

 List<Vehicle> getAvailableVehicles( LocalDateTime startDate, LocalDateTime endDate);
}
