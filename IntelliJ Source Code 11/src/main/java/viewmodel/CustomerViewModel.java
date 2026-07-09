package viewmodel;

import client.Client;
import javafx.beans.property.*;
import shared.CreateCustomerRequest;
import shared.CreateCustomerResponse;

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

    public final StringProperty statusMessage = new SimpleStringProperty();

    public void submit() {
        CreateCustomerRequest req = new CreateCustomerRequest(
                name.get(), phoneNo.get(), email.get(), cpr.get(), passportNo.get(),
                licenseNo.get(), categoryA.get(), categoryB.get(), categoryC.get(), categoryD.get()
        );

        try {
            CreateCustomerResponse res = client.createCustomer(req);
            if (res.isSuccess()) {
                statusMessage.set("Created customer #" + res.getCustomerId());
            } else {
                statusMessage.set("Failed: " + res.getMessage());
            }
        } catch (Exception e) {
            statusMessage.set("Connection error: " + e.getMessage());
        }
    }
}