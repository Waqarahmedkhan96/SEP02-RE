package shared;

import java.io.Serializable;

public class CompleteBookingResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final String message;
    private final double lateFeeCharged;

    public CompleteBookingResponse(boolean success, String message, double lateFeeCharged) {
        this.success = success;
        this.message = message;
        this.lateFeeCharged = lateFeeCharged;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public double getLateFeeCharged() { return lateFeeCharged; }
}