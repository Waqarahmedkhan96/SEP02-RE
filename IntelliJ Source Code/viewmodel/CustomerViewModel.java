package viewmodel;

import client.Client;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import shared.CreateCustomerRequest;
import shared.CreateCustomerResponse;
import shared.GetCustomersRequest;
import shared.GetCustomersResponse;
import shared.UpdateCustomerRequest;
import shared.UpdateCustomerResponse;

public class CustomerViewModel {

    private final Client client = new Client();

    public final StringProperty name = new SimpleStringProperty();
    public final StringProperty phoneNo = new SimpleStringProperty();
    public final StringProperty email = new SimpleStringProperty();
    public final StringProperty cpr = new SimpleStringProperty();
    public final StringProperty passportNo = new SimpleStringProperty();
    public final StringProperty licenseNo = new SimpleStringProperty();
    public final BooleanProperty categoryA = new SimpleBooleanProperty();
    public final BooleanProperty categoryB = new SimpleBooleanProperty();
    public final BooleanProperty categoryC = new SimpleBooleanProperty();
    public final BooleanProperty categoryD = new SimpleBooleanProperty();

    public final ObservableList<Customer> customers = FXCollections.observableArrayList();
    public final StringProperty statusMessage = new SimpleStringProperty();

    public boolean submit() {
        CreateCustomerRequest req = new CreateCustomerRequest(
                name.get(), phoneNo.get(), email.get(), cpr.get(), passportNo.get(),
                licenseNo.get(), categoryA.get(), categoryB.get(), categoryC.get(), categoryD.get()
        );

        try {
            CreateCustomerResponse res = client.createCustomer(req);
            if (res.isSuccess()) {
                statusMessage.set("Created customer #" + res.getCustomerId());
                loadCustomers();
                return true;
            } else {
                statusMessage.set("Failed: " + res.getMessage());
                return false;
            }
        } catch (Exception e) {
            statusMessage.set("Connection error: " + e.getMessage());
            return false;
        }
    }

    public void loadCustomers() {
        try {
            GetCustomersResponse res = client.getCustomers(new GetCustomersRequest());
            if (res.isSuccess()) {
                customers.setAll(res.getCustomers());
            } else {
                customers.clear();
                statusMessage.set("Failed: " + res.getMessage());
            }
        } catch (Exception e) {
            customers.clear();
            statusMessage.set("Connection error: " + e.getMessage());
        }
    }

    public boolean updateCustomer(int customerId) {
        Customer customer = new Customer(name.get(), phoneNo.get(), email.get(),
                cpr.get(), passportNo.get(), licenseNo.get());
        customer.setCustomerId(customerId);
        customer.setCategoryA(categoryA.get());
        customer.setCategoryB(categoryB.get());
        customer.setCategoryC(categoryC.get());
        customer.setCategoryD(categoryD.get());

        try {
            UpdateCustomerResponse res = client.updateCustomer(new UpdateCustomerRequest(customer));
            if (res.isSuccess()) {
                statusMessage.set("Updated customer #" + customerId);
                loadCustomers();
                return true;
            }

            statusMessage.set("Failed: " + res.getMessage());
            return false;
        } catch (Exception e) {
            statusMessage.set("Connection error: " + e.getMessage());
            return false;
        }
    }
}
