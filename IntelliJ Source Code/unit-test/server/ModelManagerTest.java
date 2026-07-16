package server;

import model.Booking;
import org.junit.jupiter.api.Test;
import shared.CreateBookingRequest;
import shared.CreateBookingResponse;
import shared.CreateCustomerRequest;
import shared.CreateCustomerResponse;
import shared.UpdateBookingRequest;
import shared.UpdateBookingResponse;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelManagerTest {

    // E - Exceptional behavior: missing customer is handled before database access.
    @Test
    void e_createBookingShouldRejectMissingCustomer() {
        ModelManager manager = new ModelManager();
        CreateBookingRequest request = new CreateBookingRequest(
                0,
                2,
                3,
                LocalDateTime.of(2026, 8, 1, 10, 0),
                LocalDateTime.of(2026, 8, 2, 10, 0)
        );

        CreateBookingResponse response = manager.createBooking(request);

        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("Customer"));
    }

    // B - Boundary behavior: start and end cannot be the same time.
    @Test
    void b_createBookingShouldRejectSameStartAndEndTime() {
        ModelManager manager = new ModelManager();
        LocalDateTime time = LocalDateTime.of(2026, 8, 1, 10, 0);

        CreateBookingResponse response = manager.createBooking(
                new CreateBookingRequest(1, 2, 3, time, time)
        );

        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("Start date"));
    }

    // E - Exceptional behavior: end before start is handled in a defined way.
    @Test
    void e_createBookingShouldRejectEndBeforeStart() {
        ModelManager manager = new ModelManager();
        LocalDateTime start = LocalDateTime.of(2026, 8, 2, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 8, 1, 10, 0);

        CreateBookingResponse response = manager.createBooking(
                new CreateBookingRequest(1, 2, 3, start, end)
        );

        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("Start date"));
    }

    // E - Exceptional behavior: missing license is handled before database access.
    @Test
    void e_createCustomerShouldRejectBlankLicenseNumber() {
        ModelManager manager = new ModelManager();
        CreateCustomerRequest request = new CreateCustomerRequest(
                "Customer",
                "12345678",
                "customer@example.com",
                "0101011234",
                "PA1234567",
                " ",
                true,
                true,
                false,
                false
        );

        CreateCustomerResponse response = manager.createCustomer(request);

        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("License"));
    }

    // E - Exceptional behavior: blank name is handled before database access.
    @Test
    void e_createCustomerShouldRejectBlankNameBeforeDatabaseAccess() {
        ModelManager manager = new ModelManager();
        CreateCustomerRequest request = new CreateCustomerRequest(
                " ",
                "12345678",
                "customer@example.com",
                "0101011234",
                "PA1234567",
                "DL12345",
                true,
                true,
                false,
                false
        );

        CreateCustomerResponse response = manager.createCustomer(request);

        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("Name"));
    }

    // M - Many / more complex: one requested booking is compared with existing active bookings.
    @Test
    void m_bookingConflictShouldDetectOverlappingActiveBooking() throws Exception {
        ModelManager manager = new ModelManager();
        Booking requested = booking(1,
                LocalDateTime.of(2026, 8, 2, 10, 0),
                LocalDateTime.of(2026, 8, 3, 10, 0),
                "ACTIVE");
        Booking existing = booking(1,
                LocalDateTime.of(2026, 8, 1, 10, 0),
                LocalDateTime.of(2026, 8, 4, 10, 0),
                "ACTIVE");

        assertTrue(hasBookingConflict(manager, requested, List.of(existing)));
    }

    // S - Simple scenario: cancelled bookings do not block a new booking.
    @Test
    void s_bookingConflictShouldIgnoreCancelledBooking() throws Exception {
        ModelManager manager = new ModelManager();
        Booking requested = booking(1,
                LocalDateTime.of(2026, 8, 2, 10, 0),
                LocalDateTime.of(2026, 8, 3, 10, 0),
                "ACTIVE");
        Booking cancelled = booking(1,
                LocalDateTime.of(2026, 8, 1, 10, 0),
                LocalDateTime.of(2026, 8, 4, 10, 0),
                "CANCELLED");

        assertFalse(hasBookingConflict(manager, requested, List.of(cancelled)));
    }

    // B - Boundary behavior: back-to-back booking periods do not overlap.
    @Test
    void b_bookingConflictShouldAllowBackToBackBookings() throws Exception {
        ModelManager manager = new ModelManager();
        Booking requested = booking(1,
                LocalDateTime.of(2026, 8, 4, 10, 0),
                LocalDateTime.of(2026, 8, 5, 10, 0),
                "ACTIVE");
        Booking existing = booking(1,
                LocalDateTime.of(2026, 8, 1, 10, 0),
                LocalDateTime.of(2026, 8, 4, 10, 0),
                "ACTIVE");

        assertFalse(hasBookingConflict(manager, requested, List.of(existing)));
    }

    // E - Exceptional behavior: update booking requires a selected booking.
    @Test
    void e_updateBookingShouldRejectMissingBookingId() {
        ModelManager manager = new ModelManager();

        UpdateBookingResponse response = manager.updateBooking(new UpdateBookingRequest(
                0,
                2,
                3,
                LocalDateTime.of(2026, 8, 1, 10, 0),
                LocalDateTime.of(2026, 8, 2, 10, 0),
                "ACTIVE"
        ));

        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("Booking"));
    }

    private Booking booking(int vehicleId, LocalDateTime start, LocalDateTime end, String status) {
        Booking booking = new Booking(start, end, status, 1, vehicleId, 1);
        booking.setBookingId(99);
        return booking;
    }

    private boolean hasBookingConflict(ModelManager manager, Booking requested, List<Booking> existing)
            throws Exception {
        Method method = ModelManager.class.getDeclaredMethod("hasBookingConflict", Booking.class, List.class);
        method.setAccessible(true);
        return (boolean) method.invoke(manager, requested, existing);
    }
}
