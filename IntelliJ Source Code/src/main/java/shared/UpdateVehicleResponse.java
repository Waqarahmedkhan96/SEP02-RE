package shared;

import java.io.Serializable;

public class UpdateVehicleResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final String message;

    public UpdateVehicleResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}
