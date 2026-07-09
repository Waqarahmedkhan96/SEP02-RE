package shared;

import java.io.Serializable;
import java.util.List;

import model.Vehicle;

public class GetVehiclesResponse implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final String message;
    private final List<Vehicle> vehicles;

    public GetVehiclesResponse(boolean success, String message, List<Vehicle> vehicles)
    {
        this.success = success;
        this.message = message;
        this.vehicles = vehicles;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public String getMessage()
    {
        return message;
    }

    public List<Vehicle> getVehicles()
    {
        return vehicles;
    }
}