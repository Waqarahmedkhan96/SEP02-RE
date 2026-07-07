package model.state;

import model.Booking;

public class ActiveState implements BookingState {
    @Override public String getStatusName() { return "ACTIVE"; }

    @Override
    public BookingState complete(Booking booking) {
        return new CompletedState(); // on-time return
    }

    @Override
    public BookingState markOverdue(Booking booking) {
        return new OverdueState();
    }

    @Override
    public BookingState update(Booking booking) {
        return this;
    }
}
