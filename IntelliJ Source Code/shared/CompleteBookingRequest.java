package shared;

import java.io.Serializable;

public class CompleteBookingRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int bookingId;

    public CompleteBookingRequest(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getBookingId() { return bookingId; }
}