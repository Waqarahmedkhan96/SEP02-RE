package model.state;

import model.Booking;

public class CancelledState implements BookingState {
    @Override public String getStatusName() { return "CANCELLED"; }

    @Override
    public BookingState complete(Booking booking) {
        throw new IllegalStateException("Cannot complete a CANCELLED booking");
    }

    @Override
    public BookingState markOverdue(Booking booking) {
        throw new IllegalStateException("Cannot mark a CANCELLED booking as overdue");
    }
}