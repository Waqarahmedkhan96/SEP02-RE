package model.state;

import model.Booking;

public class PendingState implements BookingState {
    @Override public String getStatusName() { return "PENDING"; }

    @Override
    public BookingState complete(Booking booking) {
        throw new IllegalStateException("Cannot complete a booking that has not started (PENDING)");
    }

    @Override
    public BookingState markOverdue(Booking booking) {
        throw new IllegalStateException("Cannot mark a PENDING booking as overdue");
    }

    @Override
    public BookingState update(Booking booking) {
        throw new IllegalStateException("Only ACTIVE bookings can be updated");
    }
}
