package shared;

import java.io.Serializable;

public class UpdateBookingResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final String message;

    public UpdateBookingResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}
