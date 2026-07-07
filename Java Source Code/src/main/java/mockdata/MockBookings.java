package mockdata;

import model.Booking;

import java.time.LocalDate;
import java.util.List;

public class MockBookings {

    public static List<Booking> getBookings() {
        return List.of(
                booking(1024, 1, 2, 1, "ACTIVE", "2026-07-10", "2026-07-15", null),
                booking(1027, 2, 1, 1, "ACTIVE", "2026-07-12", "2026-07-14", null),
                booking(1031, 6, 3, 2, "PENDING", "2026-07-18", "2026-07-20", null),
                booking(1034, 5, 1, 2, "PENDING", "2026-07-21", "2026-07-23", null),
                booking(1018, 4, 2, 1, "ACTIVE", "2026-07-03", "2026-07-06", null),
                booking(1020, 3, 1, 1, "OVERDUE", "2026-07-01", "2026-07-04", null),
                booking(9988, 1, 2, 3, "COMPLETED", "2026-07-01", "2026-07-02", "2026-07-02"),
                booking(9989, 2, 1, 3, "COMPLETED", "2026-07-02", "2026-07-03", "2026-07-03"),
                booking(9992, 3, 3, 3, "COMPLETED", "2026-07-03", "2026-07-04", "2026-07-04"),
                booking(1040, 7, 9, 2, "CANCELLED", "2026-07-19", "2026-07-22", null)
        );
    }

    public static long countByStatus(String status) {
        return getBookings().stream()
                .filter(booking -> status.equalsIgnoreCase(booking.getBookingStatus()))
                .count();
    }

    public static long countCompletedToday() {
        LocalDate today = LocalDate.now();
        return getBookings().stream()
                .filter(booking -> booking.getActualReturnDate() != null)
                .filter(booking -> today.equals(booking.getActualReturnDate().toLocalDate()))
                .count();
    }

    private static Booking booking(int id, int customerId, int vehicleId, int employeeId,
                                   String status, String startDate, String endDate, String actualReturnDate) {
        Booking booking = new Booking(
                LocalDate.parse(startDate).atStartOfDay(),
                LocalDate.parse(endDate).atStartOfDay(),
                status,
                customerId,
                vehicleId,
                employeeId);
        booking.setBookingId(id);
        if (actualReturnDate != null) {
            booking.setActualReturnDate(LocalDate.parse(actualReturnDate).atStartOfDay());
        }
        return booking;
    }
}
