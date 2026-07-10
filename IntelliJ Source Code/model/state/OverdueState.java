package model.state;

import model.Booking;

public class OverdueState implements BookingState {
    @Override public String getStatusName() { return "OVERDUE"; }

    @Override
    public BookingState complete(Booking booking) {
        return new CompletedState(); // late return — manager applies late fee
    }

    @Override
    public BookingState markOverdue(Booking booking) {
        throw new IllegalStateException("Booking is already OVERDUE");
    }

    @Override
    public BookingState update(Booking booking) {
        throw new IllegalStateException("Only ACTIVE bookings can be updated");
    }

    @Override
    public BookingState cancel(Booking booking) {
        throw new IllegalStateException("Cannot cancel an OVERDUE booking");
    }
}
