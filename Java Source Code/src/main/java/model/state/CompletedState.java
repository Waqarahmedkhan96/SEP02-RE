package model.state;

import model.Booking;

public class CompletedState implements BookingState {
    @Override public String getStatusName() { return "COMPLETED"; }

    @Override
    public BookingState complete(Booking booking) {
        throw new IllegalStateException("Booking is already COMPLETED");
    }

    @Override
    public BookingState markOverdue(Booking booking) {
        throw new IllegalStateException("Cannot mark a COMPLETED booking as overdue");
    }

    @Override
    public BookingState update(Booking booking) {
        throw new IllegalStateException("Only ACTIVE bookings can be updated");
    }
}
