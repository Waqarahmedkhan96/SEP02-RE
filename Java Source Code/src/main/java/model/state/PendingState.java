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
}