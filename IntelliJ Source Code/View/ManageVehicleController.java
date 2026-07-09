package view;

import java.io.IOException;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Vehicle;
import viewmodel.ManageVehicleViewModel;

public class ManageVehicleController {

    @FXML private Label titleLabel;
    @FXML private Label subtitleLabel;

    @FXML private TextField modelInput;
    @FXML private TextField vehicleTypeField;
    @FXML private TextField colorField;
    @FXML private TextField engineField;
    @FXML private TextField plateField;
    @FXML private TextField priceHourField;
    @FXML private TextField depositField;
    @FXML private TextField noOfTireField;
    @FXML private TextField lateFeeField;
    @FXML private TextField requiredLicenseField;
    @FXML private TextField conditionField;
    @FXML private TextField noOfSeatsField;
    @FXML private TextField currentStateField;

    @FXML private TableView<Vehicle> vehicleTable;
    @FXML private TableColumn<Vehicle, Number> vehicleIdColumn;
    @FXML private TableColumn<Vehicle, String> modelColumn;
    @FXML private TableColumn<Vehicle, String> vehicleTypeColumn;
    @FXML private TableColumn<Vehicle, String> colorColumn;
    @FXML private TableColumn<Vehicle, String> plateColumn;
    @FXML private TableColumn<Vehicle, String> statusColumn;

    @FXML private Label statusLabel;
    @FXML private Button addVehicleButton;
    @FXML private Button updateVehicleButton;
    @FXML private Button removeVehicleButton;

    private final ManageVehicleViewModel viewModel = new ManageVehicleViewModel();
    private String mode = "add";

    @FXML
    public void initialize() {
        vehicleIdColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getVehicleId()));
        modelColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getModel()));
        vehicleTypeColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getVehicleType()));
        colorColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getColor()));
        plateColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPlateNo()));
        statusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCurrentState()));

        vehicleTable.setItems(viewModel.vehicles);
        vehicleTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldVehicle, selectedVehicle) -> populateVehicleForm(selectedVehicle));
        vehicleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        statusLabel.textProperty().bind(viewModel.statusMessage);

        applyMode();
        viewModel.loadVehicles();
    }

    public void setMode(String mode) {
        this.mode = mode == null ? "add" : mode;
        applyMode();
    }

    @FXML
    private void handleAddVehicle() {
        try {
            Vehicle vehicle = readVehicleFromForm();
            if (viewModel.createVehicle(vehicle)) {
                clearVehicleForm();
            }
        } catch (IllegalArgumentException e) {
            viewModel.setStatusMessage(e.getMessage());
        }
    }

    @FXML
    private void handleUpdateVehicle() {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            viewModel.setStatusMessage("Select a vehicle to update.");
            return;
        }

        try {
            Vehicle vehicle = readVehicleFromForm();
            vehicle.setVehicleId(selectedVehicle.getVehicleId());

            if (viewModel.updateVehicle(vehicle)) {
                clearVehicleForm();
            }
        } catch (IllegalArgumentException e) {
            viewModel.setStatusMessage(e.getMessage());
        }
    }

    @FXML
    private void handleRemoveVehicle() {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            viewModel.setStatusMessage("Select a vehicle to remove.");
            return;
        }

        if (viewModel.removeVehicle(selectedVehicle.getVehicleId())) {
            clearVehicleForm();
        }
    }

    @FXML
    private void handleBack() throws IOException {
        Stage stage = (Stage) vehicleTable.getScene().getWindow();

        double width = stage.getWidth();
        double height = stage.getHeight();

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("VehicleManagement.fxml"));

        Parent root = loader.load();

        stage.setScene(new Scene(root));
        stage.setWidth(width);
        stage.setHeight(height);
    }

    private Vehicle readVehicleFromForm() {
        Vehicle vehicle = new Vehicle();
        vehicle.setModel(trim(modelInput));
        vehicle.setVehicleType(trim(vehicleTypeField));
        vehicle.setColor(trim(colorField));
        vehicle.setEngine(trim(engineField));
        vehicle.setPlateNo(trim(plateField));
        vehicle.setPriceHour(parseRequiredDouble(priceHourField, "Price / Hour"));
        vehicle.setDeposit(parseRequiredDouble(depositField, "Deposit"));
        vehicle.setNoOfTire(parseRequiredInt(noOfTireField, "Number of Tires"));
        vehicle.setLateFee(parseRequiredDouble(lateFeeField, "Late Fee"));
        vehicle.setRequiredLicense(trim(requiredLicenseField));
        vehicle.setCondition(trim(conditionField));
        vehicle.setNoOfSeats(parseRequiredInt(noOfSeatsField, "Number of Seats"));
        vehicle.setCurrentState(trim(currentStateField));
        return vehicle;
    }

    private void populateVehicleForm(Vehicle vehicle) {
        if (vehicle == null) {
            return;
        }

        modelInput.setText(nullToEmpty(vehicle.getModel()));
        vehicleTypeField.setText(nullToEmpty(vehicle.getVehicleType()));
        colorField.setText(nullToEmpty(vehicle.getColor()));
        engineField.setText(nullToEmpty(vehicle.getEngine()));
        plateField.setText(nullToEmpty(vehicle.getPlateNo()));
        priceHourField.setText(String.valueOf(vehicle.getPriceHour()));
        depositField.setText(String.valueOf(vehicle.getDeposit()));
        noOfTireField.setText(String.valueOf(vehicle.getNoOfTire()));
        lateFeeField.setText(String.valueOf(vehicle.getLateFee()));
        requiredLicenseField.setText(nullToEmpty(vehicle.getRequiredLicense()));
        conditionField.setText(nullToEmpty(vehicle.getCondition()));
        noOfSeatsField.setText(String.valueOf(vehicle.getNoOfSeats()));
        currentStateField.setText(nullToEmpty(vehicle.getCurrentState()));
    }

    private void clearVehicleForm() {
        vehicleTable.getSelectionModel().clearSelection();
        modelInput.clear();
        vehicleTypeField.clear();
        colorField.clear();
        engineField.clear();
        plateField.clear();
        priceHourField.clear();
        depositField.clear();
        noOfTireField.clear();
        lateFeeField.clear();
        requiredLicenseField.clear();
        conditionField.clear();
        noOfSeatsField.clear();
        currentStateField.clear();
    }

    private double parseRequiredDouble(TextField field, String fieldName) {
        String value = field.getText();
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }

        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a number.");
        }
    }

    private int parseRequiredInt(TextField field, String fieldName) {
        String value = field.getText();
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a whole number.");
        }
    }

    private String trim(TextField field) {
        String value = field.getText();
        return value == null ? null : value.trim();
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private void applyMode() {
        boolean isUpdate = "update".equalsIgnoreCase(mode);
        boolean isRemove = "remove".equalsIgnoreCase(mode);
        boolean isAdd = !isUpdate && !isRemove;

        if (titleLabel != null) {
            if (isUpdate) {
                titleLabel.setText("Update Vehicle");
                subtitleLabel.setText("Edit selected vehicle details");
            } else if (isRemove) {
                titleLabel.setText("Remove Vehicle");
                subtitleLabel.setText("Delete a vehicle without bookings");
            } else {
                titleLabel.setText("Add Vehicle");
                subtitleLabel.setText("Register a new vehicle");
            }
        }

        setButtonVisible(addVehicleButton, isAdd);
        setButtonVisible(updateVehicleButton, isUpdate);
        setButtonVisible(removeVehicleButton, isRemove);
    }

    private void setButtonVisible(Button button, boolean visible) {
        if (button != null) {
            button.setVisible(visible);
            button.setManaged(visible);
        }
    }
}
