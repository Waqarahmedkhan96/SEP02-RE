package dao;

import model.Booking;
import model.Vehicle;
import model.state.VehicleStateFactory;
import application.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {

    @Override
    public int create(Booking booking) throws SQLException {
        String insertBookingSql = "INSERT INTO booking " +
                "(start_date, end_date, actual_return_date, booking_status, customer_id, vehicle_id, employee_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING booking_id";
        String rentVehicleSql = "UPDATE vehicle SET current_state = ? WHERE vehicle_id = ?";

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

            Vehicle bookedVehicle = new Vehicle();
            bookedVehicle.setVehicleId(booking.getVehicleId());
            bookedVehicle.setCurrentState("available");
            bookedVehicle.markRented();
            try (PreparedStatement stmt = conn.prepareStatement(rentVehicleSql)) {
                stmt.setString(1, bookedVehicle.getCurrentState());
                stmt.setInt(2, bookedVehicle.getVehicleId());
                stmt.executeUpdate();
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
    public List<Booking> findAll() throws SQLException {
        String sql = "SELECT booking_id, start_date, end_date, actual_return_date, booking_status, " +
                "customer_id, vehicle_id, employee_id " +
                "FROM booking " +
                "ORDER BY start_date DESC, booking_id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            List<Booking> bookings = new ArrayList<>();
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(mapBooking(rs));
            }
            return bookings;
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

    @Override
    public List<Booking> findActiveByCustomerId(int customerId) throws SQLException {
        String sql = "SELECT booking_id, start_date, end_date, actual_return_date, booking_status, " +
                "customer_id, vehicle_id, employee_id " +
                "FROM booking " +
                "WHERE customer_id = ? AND UPPER(booking_status) = 'ACTIVE' " +
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

    @Override
    public List<Booking> searchBookings(String query, boolean cancellableOnly) throws SQLException {
        String normalizedQuery = query == null ? "" : query.trim();
        StringBuilder sql = new StringBuilder("SELECT b.booking_id, b.start_date, b.end_date, b.actual_return_date, " +
                "b.booking_status, b.customer_id, b.vehicle_id, b.employee_id " +
                "FROM booking b " +
                "JOIN customer c ON c.customer_id = b.customer_id " +
                "JOIN vehicle v ON v.vehicle_id = b.vehicle_id " +
                "WHERE 1 = 1 ");

        if (cancellableOnly) {
            sql.append("AND UPPER(b.booking_status) IN ('ACTIVE', 'PENDING') ");
        }

        boolean hasQuery = !normalizedQuery.isBlank();
        Integer numericQuery = tryParseInt(normalizedQuery);
        if (hasQuery) {
            sql.append("AND (LOWER(c.name) LIKE LOWER(?) " +
                    "OR c.phone_no LIKE ? " +
                    "OR c.email ILIKE ? " +
                    "OR c.cpr LIKE ? " +
                    "OR c.passport_no ILIKE ? " +
                    "OR v.model ILIKE ? " +
                    "OR v.vehicle_type ILIKE ? " +
                    "OR v.plate_no ILIKE ? ");
            if (numericQuery != null) {
                sql.append("OR b.booking_id = ? OR c.customer_id = ? OR b.vehicle_id = ? ");
            }
            sql.append(") ");
        }

        sql.append("ORDER BY b.start_date DESC, b.booking_id DESC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            if (hasQuery) {
                String likeQuery = "%" + normalizedQuery + "%";
                int index = 1;
                stmt.setString(index++, likeQuery);
                stmt.setString(index++, likeQuery);
                stmt.setString(index++, likeQuery);
                stmt.setString(index++, likeQuery);
                stmt.setString(index++, likeQuery);
                stmt.setString(index++, likeQuery);
                stmt.setString(index++, likeQuery);
                stmt.setString(index++, likeQuery);
                if (numericQuery != null) {
                    stmt.setInt(index++, numericQuery);
                    stmt.setInt(index++, numericQuery);
                    stmt.setInt(index, numericQuery);
                }
            }

            List<Booking> bookings = new ArrayList<>();
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(mapBooking(rs));
            }
            return bookings;
        }
    }

    @Override
    public List<Booking> searchArchivedBookings(String bookingQuery, String customerQuery, String vehicleQuery, String dateQuery) throws SQLException {
        String normalizedBooking = bookingQuery == null ? "" : bookingQuery.trim();
        String normalizedCustomer = customerQuery == null ? "" : customerQuery.trim();
        String normalizedVehicle = vehicleQuery == null ? "" : vehicleQuery.trim();
        String normalizedDate = dateQuery == null ? "" : dateQuery.trim();

        StringBuilder sql = new StringBuilder("SELECT b.booking_id, b.start_date, b.end_date, b.actual_return_date, " +
                "b.booking_status, b.customer_id, b.vehicle_id, b.employee_id, v.price_hour, " +
                "(EXTRACT(EPOCH FROM (b.end_date - b.start_date)) / 3600.0) AS total_hours, " +
                "(v.price_hour * EXTRACT(EPOCH FROM (b.end_date - b.start_date)) / 3600.0) AS total_price " +
                "FROM booking b " +
                "JOIN customer c ON c.customer_id = b.customer_id " +
                "JOIN vehicle v ON v.vehicle_id = b.vehicle_id " +
                "WHERE UPPER(b.booking_status) IN ('COMPLETED', 'CANCELLED') ");

        Integer bookingNumber = tryParseInt(normalizedBooking);
        Integer customerNumber = tryParseInt(normalizedCustomer);
        Integer vehicleNumber = tryParseInt(normalizedVehicle);
        if (!normalizedBooking.isBlank()) {
            if (bookingNumber != null) {
                sql.append("AND b.booking_id = ? ");
            } else {
                sql.append("AND CAST(b.booking_id AS TEXT) LIKE ? ");
            }
        }

        if (!normalizedCustomer.isBlank()) {
            sql.append("AND (CAST(c.customer_id AS TEXT) LIKE ? OR c.name ILIKE ? OR c.phone_no ILIKE ? OR c.email ILIKE ? OR c.cpr ILIKE ? OR c.passport_no ILIKE ? ");
            if (customerNumber != null) {
                sql.append("OR c.customer_id = ? ");
            }
            sql.append(") ");
        }

        if (!normalizedVehicle.isBlank()) {
            sql.append("AND (v.model ILIKE ? OR v.vehicle_type ILIKE ? OR v.plate_no ILIKE ? ");
            if (vehicleNumber != null) {
                sql.append("OR v.vehicle_id = ? ");
            }
            sql.append(") ");
        }

        if (!normalizedDate.isBlank()) {
            sql.append("AND (TO_CHAR(b.start_date, 'YYYY-MM-DD') LIKE ? " +
                    "OR TO_CHAR(b.end_date, 'YYYY-MM-DD') LIKE ? " +
                    "OR TO_CHAR(b.actual_return_date, 'YYYY-MM-DD') LIKE ?) ");
        }

        sql.append("ORDER BY COALESCE(b.actual_return_date, b.end_date) DESC, b.start_date DESC, b.booking_id DESC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int index = 1;
            if (!normalizedBooking.isBlank()) {
                if (bookingNumber != null) {
                    stmt.setInt(index++, bookingNumber);
                } else {
                    stmt.setString(index++, "%" + normalizedBooking + "%");
                }
            }

            if (!normalizedCustomer.isBlank()) {
                String likeCustomer = "%" + normalizedCustomer + "%";
                stmt.setString(index++, likeCustomer);
                stmt.setString(index++, likeCustomer);
                stmt.setString(index++, likeCustomer);
                stmt.setString(index++, likeCustomer);
                stmt.setString(index++, likeCustomer);
                if (customerNumber != null) {
                    stmt.setInt(index++, customerNumber);
                }
            }

            if (!normalizedVehicle.isBlank()) {
                String likeVehicle = "%" + normalizedVehicle + "%";
                stmt.setString(index++, likeVehicle);
                stmt.setString(index++, likeVehicle);
                stmt.setString(index++, likeVehicle);
                if (vehicleNumber != null) {
                    stmt.setInt(index++, vehicleNumber);
                }
            }

            if (!normalizedDate.isBlank()) {
                String likeDate = "%" + normalizedDate + "%";
                stmt.setString(index++, likeDate);
                stmt.setString(index++, likeDate);
                stmt.setString(index, likeDate);
            }

            List<Booking> bookings = new ArrayList<>();
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(mapBooking(rs));
            }
            return bookings;
        }
    }

    @Override
    public Booking findById(int bookingId) throws SQLException {
        String sql = "SELECT booking_id, start_date, end_date, actual_return_date, booking_status, " +
                "customer_id, vehicle_id, employee_id " +
                "FROM booking WHERE booking_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? mapBooking(rs) : null;
        }
    }

    @Override
    public void updateActiveBooking(Booking booking) throws SQLException {
        String updateBookingSql = "UPDATE booking " +
                "SET start_date = ?, end_date = ?, booking_status = ?, vehicle_id = ?, employee_id = ? " +
                "WHERE booking_id = ? AND UPPER(booking_status) = 'ACTIVE'";
        String updateOldVehicleSql = "UPDATE vehicle SET current_state = ? WHERE vehicle_id = ?";
        String updateNewVehicleSql = "UPDATE vehicle SET current_state = ? WHERE vehicle_id = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            Booking existing = findByIdForUpdate(conn, booking.getBookingId());
            if (existing == null) {
                throw new SQLException("Booking cannot be found");
            }
            if (!"ACTIVE".equalsIgnoreCase(existing.getBookingStatus())) {
                throw new SQLException("Only ACTIVE bookings can be updated");
            }

            ensureExists(conn, "vehicle", "vehicle_id", booking.getVehicleId(), "Vehicle does not exist");
            ensureExists(conn, "employee", "employee_id", booking.getEmployeeId(), "Employee does not exist");

            if (!isVehicleAvailableForUpdate(conn, booking, existing.getBookingId(), existing.getVehicleId())) {
                throw new SQLException("Vehicle is not available for the selected period");
            }

            try (PreparedStatement stmt = conn.prepareStatement(updateBookingSql)) {
                stmt.setTimestamp(1, Timestamp.valueOf(booking.getStartDate()));
                stmt.setTimestamp(2, Timestamp.valueOf(booking.getEndDate()));
                stmt.setString(3, booking.getBookingStatus());
                stmt.setInt(4, booking.getVehicleId());
                stmt.setInt(5, booking.getEmployeeId());
                stmt.setInt(6, booking.getBookingId());
                int updated = stmt.executeUpdate();
                if (updated == 0) {
                    throw new SQLException("Booking was changed by another employee before confirmation");
                }
            }

            if (existing.getVehicleId() != booking.getVehicleId()) {
                Vehicle oldVehicle = new Vehicle();
                oldVehicle.setVehicleId(existing.getVehicleId());
                oldVehicle.setCurrentState("rented");
                oldVehicle.markAvailable();
                try (PreparedStatement stmt = conn.prepareStatement(updateOldVehicleSql)) {
                    stmt.setString(1, oldVehicle.getCurrentState());
                    stmt.setInt(2, oldVehicle.getVehicleId());
                    stmt.executeUpdate();
                }

                Vehicle newVehicle = new Vehicle();
                newVehicle.setVehicleId(booking.getVehicleId());
                newVehicle.setCurrentState("available");
                newVehicle.markRented();
                try (PreparedStatement stmt = conn.prepareStatement(updateNewVehicleSql)) {
                    stmt.setString(1, newVehicle.getCurrentState());
                    stmt.setInt(2, newVehicle.getVehicleId());
                    stmt.executeUpdate();
                }
            }

            conn.commit();
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
    public void cancelBooking(int bookingId, int vehicleId, String newStatus) throws SQLException {
        String updateBookingSql = "UPDATE booking SET booking_status = ? " +
                "WHERE booking_id = ? AND UPPER(booking_status) IN ('ACTIVE', 'PENDING')";
        String updateVehicleSql = "UPDATE vehicle SET current_state = 'available' WHERE vehicle_id = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(updateBookingSql)) {
                stmt.setString(1, newStatus);
                stmt.setInt(2, bookingId);
                int updated = stmt.executeUpdate();
                if (updated == 0) {
                    throw new SQLException("Booking was changed by another employee before confirmation");
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(updateVehicleSql)) {
                stmt.setInt(1, vehicleId);
                stmt.executeUpdate();
            }

            conn.commit();
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
    public List<Booking> findOverdueCandidates() throws SQLException {
        // ACTIVE bookings whose endDate has already passed but haven't been marked OVERDUE yet
        String sql = "SELECT booking_id, start_date, end_date, actual_return_date, booking_status, " +
                "customer_id, vehicle_id, employee_id " +
                "FROM booking " +
                "WHERE UPPER(booking_status) = 'ACTIVE' AND end_date < ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ResultSet rs = stmt.executeQuery();

            List<Booking> overdue = new ArrayList<>();
            while (rs.next()) {
                overdue.add(mapBooking(rs));
            }
            return overdue;
        }
    }

    @Override
    public void markOverdue(int bookingId) throws SQLException {
        String sql = "UPDATE booking SET booking_status = 'OVERDUE' WHERE booking_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void completeBooking(int bookingId, int vehicleId, LocalDateTime actualReturnDate, String newStatus) throws SQLException {
        String updateBookingSql = "UPDATE booking SET booking_status = ?, actual_return_date = ? WHERE booking_id = ?";
        String updateVehicleSql = "UPDATE vehicle SET current_state = ? WHERE vehicle_id = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(updateBookingSql)) {
                stmt.setString(1, newStatus);
                stmt.setTimestamp(2, Timestamp.valueOf(actualReturnDate));
                stmt.setInt(3, bookingId);
                stmt.executeUpdate();
            }

            Vehicle vehicle = new Vehicle();
            vehicle.setVehicleId(vehicleId);
            vehicle.setCurrentState("rented");
            vehicle.markAvailable();
            try (PreparedStatement stmt = conn.prepareStatement(updateVehicleSql)) {
                stmt.setString(1, vehicle.getCurrentState());
                stmt.setInt(2, vehicle.getVehicleId());
                stmt.executeUpdate();
            }

            conn.commit();
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
    public double getVehicleLateFeeRate(int vehicleId) throws SQLException {
        String sql = "SELECT late_fee FROM vehicle WHERE vehicle_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) throw new SQLException("Vehicle does not exist");
            return rs.getDouble("late_fee");
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
            if (!VehicleStateFactory.fromState(rs.getString("current_state")).canBeBooked()) {
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

    private Booking findByIdForUpdate(Connection conn, int bookingId) throws SQLException {
        String sql = "SELECT booking_id, start_date, end_date, actual_return_date, booking_status, " +
                "customer_id, vehicle_id, employee_id " +
                "FROM booking WHERE booking_id = ? FOR UPDATE";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? mapBooking(rs) : null;
        }
    }

    private boolean isVehicleAvailableForUpdate(Connection conn, Booking booking, int excludedBookingId, int originalVehicleId) throws SQLException {
        if (booking.getVehicleId() != originalVehicleId) {
            String vehicleStateSql = "SELECT current_state FROM vehicle WHERE vehicle_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(vehicleStateSql)) {
                stmt.setInt(1, booking.getVehicleId());
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    return false;
                }
                if (!VehicleStateFactory.fromState(rs.getString("current_state")).canBeBooked()) {
                    return false;
                }
            }
        }

        String overlapSql = "SELECT 1 FROM booking " +
                "WHERE vehicle_id = ? " +
                "AND booking_id <> ? " +
                "AND UPPER(booking_status) NOT IN ('CANCELLED', 'COMPLETED') " +
                "AND start_date < ? " +
                "AND end_date > ? " +
                "LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(overlapSql)) {
            stmt.setInt(1, booking.getVehicleId());
            stmt.setInt(2, excludedBookingId);
            stmt.setTimestamp(3, Timestamp.valueOf(booking.getEndDate()));
            stmt.setTimestamp(4, Timestamp.valueOf(booking.getStartDate()));
            ResultSet rs = stmt.executeQuery();
            return !rs.next();
        }
    }

    private Integer tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
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
        if (hasColumn(rs, "price_hour")) {
            booking.setPriceHour(rs.getDouble("price_hour"));
        }
        if (hasColumn(rs, "total_hours")) {
            booking.setTotalHours(rs.getDouble("total_hours"));
        }
        if (hasColumn(rs, "total_price")) {
            booking.setTotalPrice(rs.getDouble("total_price"));
        }
        return booking;
    }

    private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData metadata = rs.getMetaData();
        for (int i = 1; i <= metadata.getColumnCount(); i++) {
            if (columnName.equalsIgnoreCase(metadata.getColumnLabel(i))) {
                return true;
            }
        }
        return false;
    }
}
