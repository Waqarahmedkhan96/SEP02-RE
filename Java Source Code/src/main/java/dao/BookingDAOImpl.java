package dao;

import model.Booking;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {

    @Override
    public int create(Booking booking) throws SQLException {
        String insertBookingSql = "INSERT INTO booking " +
                "(start_date, end_date, actual_return_date, booking_status, customer_id, vehicle_id, employee_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING booking_id";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            ensureExists(conn, "customer", "customer_id", booking.getCustomerId(), "Customer does not exist");
            ensureExists(conn, "vehicle", "vehicle_id", booking.getVehicleId(), "Vehicle does not exist");
            ensureExists(conn, "employee", "employee_id", booking.getEmployeeId(), "Employee does not exist");

            if (!isVehicleAvailable(conn, booking)) {
                throw new SQLException("Vehicle is not available for the selected period");
            }

            int generatedId;
            try (PreparedStatement bookingStmt = conn.prepareStatement(insertBookingSql)) {
                bookingStmt.setTimestamp(1, Timestamp.valueOf(booking.getStartDate()));
                bookingStmt.setTimestamp(2, Timestamp.valueOf(booking.getEndDate()));
                if (booking.getActualReturnDate() == null) {
                    bookingStmt.setTimestamp(3, null);
                } else {
                    bookingStmt.setTimestamp(3, Timestamp.valueOf(booking.getActualReturnDate()));
                }
                bookingStmt.setString(4, booking.getBookingStatus());
                bookingStmt.setInt(5, booking.getCustomerId());
                bookingStmt.setInt(6, booking.getVehicleId());
                bookingStmt.setInt(7, booking.getEmployeeId());

                ResultSet rs = bookingStmt.executeQuery();
                rs.next();
                generatedId = rs.getInt("booking_id");
            }

            conn.commit();
            return generatedId;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public List<Booking> findByCustomerId(int customerId) throws SQLException {
        String sql = "SELECT booking_id, start_date, end_date, actual_return_date, booking_status, " +
                "customer_id, vehicle_id, employee_id " +
                "FROM booking " +
                "WHERE customer_id = ? " +
                "ORDER BY start_date DESC, booking_id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ensureExists(conn, "customer", "customer_id", customerId, "Customer does not exist");
            stmt.setInt(1, customerId);

            List<Booking> bookings = new ArrayList<>();
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(mapBooking(rs));
            }
            return bookings;
        }
    }

    private void ensureExists(Connection conn, String tableName, String idColumn, int id, String message) throws SQLException {
        String sql = "SELECT 1 FROM " + tableName + " WHERE " + idColumn + " = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new SQLException(message);
            }
        }
    }

    private boolean isVehicleAvailable(Connection conn, Booking booking) throws SQLException {
        String vehicleStateSql = "SELECT current_state FROM vehicle WHERE vehicle_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(vehicleStateSql)) {
            stmt.setInt(1, booking.getVehicleId());
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return false;
            }
            String state = rs.getString("current_state");
            if (state != null && !state.equalsIgnoreCase("available")) {
                return false;
            }
        }

        String overlapSql = "SELECT 1 FROM booking " +
                "WHERE vehicle_id = ? " +
                "AND UPPER(booking_status) NOT IN ('CANCELLED', 'COMPLETED') " +
                "AND start_date < ? " +
                "AND end_date > ? " +
                "LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(overlapSql)) {
            stmt.setInt(1, booking.getVehicleId());
            stmt.setTimestamp(2, Timestamp.valueOf(booking.getEndDate()));
            stmt.setTimestamp(3, Timestamp.valueOf(booking.getStartDate()));
            ResultSet rs = stmt.executeQuery();
            return !rs.next();
        }
    }

    private Booking mapBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(rs.getInt("booking_id"));
        booking.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());
        booking.setEndDate(rs.getTimestamp("end_date").toLocalDateTime());

        Timestamp actualReturnDate = rs.getTimestamp("actual_return_date");
        if (actualReturnDate != null) {
            booking.setActualReturnDate(actualReturnDate.toLocalDateTime());
        }

        booking.setBookingStatus(rs.getString("booking_status"));
        booking.setCustomerId(rs.getInt("customer_id"));
        booking.setVehicleId(rs.getInt("vehicle_id"));
        booking.setEmployeeId(rs.getInt("employee_id"));
        return booking;
    }
}
