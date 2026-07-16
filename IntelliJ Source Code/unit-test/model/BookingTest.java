package model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BookingTest {

    // Z - Zero: simple post-conditions of a just created object.
    @Test
    void z_newBookingShouldHaveZeroPaymentValues() {
        Booking booking = new Booking();

        assertEquals(0.0, booking.getBookedHours());
        assertEquals(0.0, booking.getLateHours());
        assertEquals(0.0, booking.getTotalHours());
        assertEquals(0.0, booking.getTotalPrice());
        assertNull(booking.getActualReturnDate());
    }

    // O - One: one booking stores one selected customer, vehicle, and employee.
    @Test
    void o_shouldCreateOneBookingWithSelectedEntities() {
        LocalDateTime start = LocalDateTime.of(2026, 8, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 8, 3, 10, 0);

        Booking booking = new Booking(start, end, "ACTIVE", 1, 2, 3);

        assertEquals(start, booking.getStartDate());
        assertEquals(end, booking.getEndDate());
        assertEquals("ACTIVE", booking.getBookingStatus());
        assertEquals(1, booking.getCustomerId());
        assertEquals(2, booking.getVehicleId());
        assertEquals(3, booking.getEmployeeId());
    }

    // B - Boundary: the model object can hold a same-time period; ModelManager validates whether it is allowed.
    @Test
    void b_bookingCanStoreSameStartAndEndTime() {
        LocalDateTime time = LocalDateTime.of(2026, 8, 1, 10, 0);

        Booking booking = new Booking(time, time, "ACTIVE", 1, 2, 3);

        assertEquals(time, booking.getStartDate());
        assertEquals(time, booking.getEndDate());
    }

    // M - Many / more complex: several price fields are updated for a completed booking.
    @Test
    void m_shouldStoreReturnAndPriceCalculationFields() {
        LocalDateTime returnDate = LocalDateTime.of(2026, 8, 4, 12, 0);
        Booking booking = new Booking();

        booking.setActualReturnDate(returnDate);
        booking.setPriceHour(120.0);
        booking.setBookedHours(48.0);
        booking.setActualHours(50.0);
        booking.setLateHours(2.0);
        booking.setTotalHours(50.0);
        booking.setTotalPrice(6000.0);

        assertEquals(returnDate, booking.getActualReturnDate());
        assertEquals(120.0, booking.getPriceHour());
        assertEquals(48.0, booking.getBookedHours());
        assertEquals(50.0, booking.getActualHours());
        assertEquals(2.0, booking.getLateHours());
        assertEquals(50.0, booking.getTotalHours());
        assertEquals(6000.0, booking.getTotalPrice());
    }
}
