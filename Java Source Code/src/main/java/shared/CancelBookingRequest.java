package shared;

import java.io.Serializable;

public class CancelBookingRequest implements Serializable {
    private final int bookingId;

    public CancelBookingRequest(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getBookingId() {
        return bookingId;
    }
}
