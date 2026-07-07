package dao;

import model.Vehicle;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAOImpl implements VehicleDAO {

    @Override
    public int create(Vehicle vehicle) throws SQLException {
        String sql = "INSERT INTO vehicle " +
                "(model, color, engine, plate_no, price_hour, deposit, no_of_tire, late_fee, " +
                "required_license, condition, no_of_seats, current_state) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING vehicle_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setVehicleFields(stmt, vehicle);

            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt("vehicle_id");
        }
    }

    @Override
    public void update(Vehicle vehicle) throws SQLException {
        String sql = "UPDATE vehicle SET " +
                "model = ?, color = ?, engine = ?, plate_no = ?, price_hour = ?, deposit = ?, " +
                "no_of_tire = ?, late_fee = ?, required_license = ?, condition = ?, " +
                "no_of_seats = ?, current_state = ? " +
                "WHERE vehicle_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setVehicleFields(stmt, vehicle);
            stmt.setInt(13, vehicle.getVehicleId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Vehicle does not exist");
            }
        }
    }

    @Override
    public void delete(int vehicleId) throws SQLException {
        String sql = "DELETE FROM vehicle WHERE vehicle_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehicleId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Vehicle does not exist");
            }
        }
    }

    @Override
    public List<Vehicle> findAll() throws SQLException {
        String sql = "SELECT vehicle_id, model, color, engine, plate_no, price_hour, deposit, " +
                "no_of_tire, late_fee, required_license, condition, no_of_seats, current_state " +
                "FROM vehicle ORDER BY vehicle_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            List<Vehicle> vehicles = new ArrayList<>();

            while (rs.next()) {
                vehicles.add(mapVehicle(rs));
            }

            return vehicles;
        }
    }

    private void setVehicleFields(PreparedStatement stmt, Vehicle vehicle) throws SQLException {
        stmt.setString(1, vehicle.getModel());
        stmt.setString(2, vehicle.getColor());
        stmt.setString(3, vehicle.getEngine());
        stmt.setString(4, vehicle.getPlateNo());
        stmt.setDouble(5, vehicle.getPriceHour());
        stmt.setDouble(6, vehicle.getDeposit());
        stmt.setInt(7, vehicle.getNoOfTire());
        stmt.setDouble(8, vehicle.getLateFee());
        stmt.setString(9, vehicle.getRequiredLicense());
        stmt.setString(10, vehicle.getCondition());
        stmt.setInt(11, vehicle.getNoOfSeats());
        stmt.setString(12, vehicle.getCurrentState());
    }

    private Vehicle mapVehicle(ResultSet rs) throws SQLException {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(rs.getInt("vehicle_id"));
        vehicle.setModel(rs.getString("model"));
        vehicle.setColor(rs.getString("color"));
        vehicle.setEngine(rs.getString("engine"));
        vehicle.setPlateNo(rs.getString("plate_no"));
        vehicle.setPriceHour(rs.getDouble("price_hour"));
        vehicle.setDeposit(rs.getDouble("deposit"));
        vehicle.setNoOfTire(rs.getInt("no_of_tire"));
        vehicle.setLateFee(rs.getDouble("late_fee"));
        vehicle.setRequiredLicense(rs.getString("required_license"));
        vehicle.setCondition(rs.getString("condition"));
        vehicle.setNoOfSeats(rs.getInt("no_of_seats"));
        vehicle.setCurrentState(rs.getString("current_state"));
        return vehicle;
    }
}
