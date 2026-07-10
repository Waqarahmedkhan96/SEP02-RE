package shared;

import java.io.Serializable;

public class GetCustomerBookingsRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int customerId;
    private final boolean activeOnly;

    public GetCustomerBookingsRequest(int customerId) {
        this(customerId, false);
    }

    public GetCustomerBookingsRequest(int customerId, boolean activeOnly) {
        this.customerId = customerId;
        this.activeOnly = activeOnly;
    }

    public int getCustomerId() {
        return customerId;
    }

    public boolean isActiveOnly() {
        return activeOnly;
    }
}
