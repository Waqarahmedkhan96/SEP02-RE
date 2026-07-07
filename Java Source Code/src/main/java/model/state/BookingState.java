package model.state;

import model.Booking;

public interface BookingState {
    String getStatusName();

    /** Attempt to complete the booking from this state. Returns the next state. */
    BookingState complete(Booking booking);

    /** Attempt to mark the booking overdue from this state. Returns the next state. */
    BookingState markOverdue(Booking booking);

    /** Attempt to update booking details from this state. Returns the current state if allowed. */
    BookingState update(Booking booking);

    /** Attempt to cancel the booking from this state. Returns the next state if allowed. */
    BookingState cancel(Booking booking);
}
