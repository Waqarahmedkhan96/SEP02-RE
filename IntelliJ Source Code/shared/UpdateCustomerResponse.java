package shared;

import java.io.Serializable;

public class UpdateCustomerResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final String message;

    public UpdateCustomerResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
