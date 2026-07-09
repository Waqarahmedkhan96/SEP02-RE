package shared;

import model.Customer;

import java.io.Serializable;
import java.util.List;

public class GetCustomersResponse implements Serializable {
    private final boolean success;
    private final String message;
    private final List<Customer> customers;

    public GetCustomersResponse(boolean success, String message, List<Customer> customers) {
        this.success = success;
        this.message = message;
        this.customers = customers;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Customer> getCustomers() {
        return customers;
    }
}
