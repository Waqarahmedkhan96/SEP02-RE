package shared;

import java.io.Serializable;

import model.Customer;

public class UpdateCustomerRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Customer customer;

    public UpdateCustomerRequest(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}
