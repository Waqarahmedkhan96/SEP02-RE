package viewmodel;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Vehicle;
import shared.CreateVehicleRequest;
import shared.CreateVehicleResponse;
import shared.GetVehiclesRequest;
import shared.GetVehiclesResponse;
import shared.RemoveVehicleRequest;
import shared.RemoveVehicleResponse;
import shared.UpdateVehicleRequest;
import shared.UpdateVehicleResponse;

public class ManageVehicleViewModel {

    private final Client client = new Client();

    public final ObservableList<Vehicle> vehicles =
            FXCollections.observableArrayList();

    public final javafx.beans.property.StringProperty statusMessage =
            new javafx.beans.property.SimpleStringProperty();

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

    public boolean createVehicle(Vehicle vehicle) {
        try {
            CreateVehicleResponse response =
                    client.createVehicle(new CreateVehicleRequest(vehicle));

            if (response.isSuccess()) {
                loadVehicles();
                statusMessage.set(response.getMessage() + " (ID: " + response.getVehicleId() + ").");
                return true;
            }

            statusMessage.set(response.getMessage());
            return false;
        } catch (Exception e) {
            statusMessage.set(e.getMessage());
            return false;
        }
    }

    public boolean updateVehicle(Vehicle vehicle) {
        try {
            UpdateVehicleResponse response =
                    client.updateVehicle(new UpdateVehicleRequest(vehicle));

            if (response.isSuccess()) {
                loadVehicles();
            }

            statusMessage.set(response.getMessage());
            return response.isSuccess();
        } catch (Exception e) {
            statusMessage.set(e.getMessage());
            return false;
        }
    }

    public boolean removeVehicle(int vehicleId) {
        try {
            RemoveVehicleResponse response =
                    client.removeVehicle(new RemoveVehicleRequest(vehicleId));

            if (response.isSuccess()) {
                loadVehicles();
            }

            statusMessage.set(response.getMessage());
            return response.isSuccess();
        } catch (Exception e) {
            statusMessage.set(e.getMessage());
            return false;
        }
    }

    public void setStatusMessage(String message) {
        statusMessage.set(message);
    }
}
