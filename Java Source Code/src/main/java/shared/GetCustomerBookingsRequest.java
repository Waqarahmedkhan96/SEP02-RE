package shared;

import java.io.Serializable;

public class GetCustomerBookingsRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int customerId;

    public GetCustomerBookingsRequest(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }
}
