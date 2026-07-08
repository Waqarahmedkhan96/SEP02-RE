package shared;

import model.Booking;

import java.io.Serializable;
import java.util.List;

public class SearchBookingsResponse implements Serializable {
    private final boolean success;
    private final String message;
    private final List<Booking> bookings;

    public SearchBookingsResponse(boolean success, String message, List<Booking> bookings) {
        this.success = success;
        this.message = message;
        this.bookings = bookings;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Booking> getBookings() {
        return bookings;
    }
}
