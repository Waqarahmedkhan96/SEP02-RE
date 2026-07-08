package dao;

import model.Booking;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingDAO {
    int create(Booking booking) throws SQLException;
    List<Booking> findByCustomerId(int customerId) throws SQLException;
    List<Booking> findActiveByCustomerId(int customerId) throws SQLException;
    List<Booking> searchBookings(String query, boolean cancellableOnly) throws SQLException;
    List<Booking> searchArchivedBookings(String customerQuery, String vehicleQuery, String dateQuery) throws SQLException;

    Booking findById(int bookingId) throws SQLException;
    void updateActiveBooking(Booking booking) throws SQLException;
    void cancelBooking(int bookingId, int vehicleId, String newStatus) throws SQLException;
    List<Booking> findOverdueCandidates() throws SQLException;
    void markOverdue(int bookingId) throws SQLException;
    void completeBooking(int bookingId, int vehicleId, LocalDateTime actualReturnDate, String newStatus) throws SQLException;
    double getVehicleLateFeeRate(int vehicleId) throws SQLException;
}
