package shared;

import java.io.Serializable;

public class CreateCustomerResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String message;
    private int customerId;

    public CreateCustomerResponse(boolean success, String message, int customerId) {
        this.success = success;
        this.message = message;
        this.customerId = customerId;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public int getCustomerId() { return customerId; }
}