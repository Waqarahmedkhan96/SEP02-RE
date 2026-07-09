package shared;

import java.io.Serializable;

public class CreateBookingResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String message;
    private int bookingId;

    public CreateBookingResponse(boolean success, String message, int bookingId) {
        this.success = success;
        this.message = message;
        this.bookingId = bookingId;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public int getBookingId() { return bookingId; }
}
