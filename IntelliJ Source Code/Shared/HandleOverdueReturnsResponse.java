package shared;

import java.io.Serializable;
import java.util.List;

public class HandleOverdueReturnsResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final String message;
    private final List<Integer> overdueBookingIds;

    public HandleOverdueReturnsResponse(boolean success, String message, List<Integer> overdueBookingIds) {
        this.success = success;
        this.message = message;
        this.overdueBookingIds = overdueBookingIds;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<Integer> getOverdueBookingIds() { return overdueBookingIds; }
}