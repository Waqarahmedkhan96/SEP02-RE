package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.Vehicle;
import utils.DatabaseConnection;

public class VehicleDAOImpl implements VehicleDAO {
@Override
    public List<Vehicle> getAllVehicles()
    {
        String sql = "SELECT * FROM vehicle";

        List<Vehicle> vehicles = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery())
        {

            while (rs.next())
            {
                Vehicle vehicle = new Vehicle();

                vehicle.setVehicleId(rs.getInt("vehicle_id"));
                vehicle.setModel(rs.getString("model"));
                vehicle.setVehicleType(rs.getString("vehicle_type"));
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

                vehicles.add(vehicle);
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return vehicles;
    }

    @Override
    public List<Vehicle> filterVehicles(String color, String vehicleType, String status, double maxPrice)
    {
        String sql =
                "SELECT * FROM vehicle " +
                "WHERE color = ? " +
                "AND vehicle_type = ? " +
                "AND current_state = ? " +
                "AND price_hour <= ?";

        List<Vehicle> vehicles = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {

            stmt.setString(1, color);
            stmt.setString(2, vehicleType);
            stmt.setString(2, status);
            stmt.setDouble(3, maxPrice);

            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                Vehicle vehicle = new Vehicle();

                vehicle.setVehicleId(rs.getInt("vehicle_id"));
                vehicle.setModel(rs.getString("model"));
                vehicle.setVehicleType(rs.getString("vehicle_type"));
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

                vehicles.add(vehicle);
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return vehicles;
    }

    @Override
    public Vehicle getVehicleById(int id)
    {
        String sql = "SELECT * FROM vehicle WHERE vehicle_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                Vehicle vehicle = new Vehicle();

                vehicle.setVehicleId(rs.getInt("vehicle_id"));
                vehicle.setModel(rs.getString("model"));
                vehicle.setVehicleType(rs.getString("vehicle_type"));
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
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }
    @Override
public List<Vehicle> getAvailableVehicles(LocalDateTime startDate, LocalDateTime endDate)
{
    String sql =
            "SELECT * FROM vehicle v " +
            "WHERE current_state='available' " +
            "AND NOT EXISTS (" +
            "SELECT 1 FROM booking b " +
            "WHERE b.vehicle_id=v.vehicle_id " +
            "AND UPPER(b.booking_status) NOT IN ('COMPLETED','CANCELLED') " +
            "AND b.start_date < ? " +
            "AND b.end_date > ?)";

    List<Vehicle> vehicles = new ArrayList<>();

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql))
    {

        stmt.setTimestamp(1, Timestamp.valueOf(endDate));
        stmt.setTimestamp(2, Timestamp.valueOf(startDate));

        ResultSet rs = stmt.executeQuery();

        while (rs.next())
        {
            Vehicle vehicle = new Vehicle();

            vehicle.setVehicleId(rs.getInt("vehicle_id"));
            vehicle.setModel(rs.getString("model"));
            vehicle.setVehicleType(rs.getString("vehicle_type"));
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

            vehicles.add(vehicle);
        }

    }
    catch (SQLException e)
    {
        e.printStackTrace();
    }

    return vehicles;
}
}
