package shared;

import java.io.Serializable;

public class FilterVehiclesRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String color;
    private final String vehicleType;
    private final String status;
    private final double maxPrice;

    public FilterVehiclesRequest(String color, String vehicleType,String status,double maxPrice) {

    this.color = color;
    this.vehicleType = vehicleType;
    this.status = status;
    this.maxPrice = maxPrice;
    }

    public String getColor() {
        return color;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getStatus() {
        return status;
    }

    public double getMaxPrice() {
        return maxPrice;
    }
}