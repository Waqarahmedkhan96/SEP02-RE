package dao;

import model.Vehicle;

import java.sql.SQLException;
import java.util.List;

public interface VehicleDAO {
    int create(Vehicle vehicle) throws SQLException;
    void update(Vehicle vehicle) throws SQLException;
    void delete(int vehicleId) throws SQLException;
    List<Vehicle> findAll() throws SQLException;
}
