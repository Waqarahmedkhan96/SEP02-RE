package viewmodel;

import client.Client;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Vehicle;
import shared.FilterVehiclesRequest;
import shared.FilterVehiclesResponse;
import shared.GetVehiclesRequest;
import shared.GetVehiclesResponse;
import java.time.LocalDateTime;
import shared.CheckAvailabilityRequest;
import shared.CheckAvailabilityResponse;

public class VehicleViewModel {

    private final Client client = new Client();

    public final StringProperty color = new SimpleStringProperty();
    public final StringProperty status = new SimpleStringProperty();
    public final DoubleProperty maxPrice = new SimpleDoubleProperty();
    public final StringProperty vehicleType = new SimpleStringProperty();
    public final StringProperty startDate = new SimpleStringProperty();
    public final StringProperty endDate = new SimpleStringProperty();

    public final StringProperty statusMessage = new SimpleStringProperty();

    public final ObservableList<Vehicle> vehicles =
            FXCollections.observableArrayList();

    public void loadVehicles() {
        try {

            GetVehiclesResponse response =
                    client.getVehicles(new GetVehiclesRequest());

            if (response.isSuccess()) {
                vehicles.setAll(response.getVehicles());
                statusMessage.set("Loaded " + response.getVehicles().size() + " vehicles.");
            } else {
                vehicles.clear();
                statusMessage.set(response.getMessage());
            }

        } catch (Exception e) {
            vehicles.clear();
            statusMessage.set(e.getMessage());
        }
    }

    public void filterVehicles() {

        try {

            FilterVehiclesResponse response = client.filterVehicles(
                            new FilterVehiclesRequest(
                                    color.get(),
                                    vehicleType.get(),
                                    status.get(),
                                    maxPrice.get()
                            ));

            if (response.isSuccess()) {

                vehicles.setAll(response.getVehicles());
                statusMessage.set(response.getVehicles().size() + " vehicle(s) found.");

            } else {

                vehicles.clear();
                statusMessage.set(response.getMessage());

            }

        } catch (Exception e) {

            vehicles.clear();
            statusMessage.set(e.getMessage());

        }

    }

  public void checkAvailability() {

    try {

        LocalDateTime start = LocalDateTime.parse(startDate.get());
        LocalDateTime end = LocalDateTime.parse(endDate.get());

        CheckAvailabilityResponse response =
                client.checkAvailability(
                        new CheckAvailabilityRequest(start, end));

        vehicles.setAll(response.getAvailableVehicles());

        statusMessage.set(response.getAvailableVehicles().size()
                + " vehicle(s) available.");

    }
    catch (Exception e)
    {
        vehicles.clear();
        statusMessage.set(e.getMessage());
    }
}
}