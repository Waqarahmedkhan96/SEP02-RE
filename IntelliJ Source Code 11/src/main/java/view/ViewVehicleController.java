package view;

import java.io.IOException;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Vehicle;
import viewmodel.ViewVehicleViewModel;


public class ViewVehicleController {

    @FXML private TextField colorField;
    @FXML private TextField vehicleTypeField;
    @FXML private TextField statusField;
    @FXML private TextField maxPriceField;

    @FXML private TextField startDateField;
    @FXML private TextField endDateField;

    @FXML private TableView<Vehicle> vehicleTable;

    @FXML private TableColumn<Vehicle, Number> vehicleIdColumn;
    @FXML private TableColumn<Vehicle, String> modelColumn;
    @FXML private TableColumn<Vehicle, String> vehicleTypeColumn;
    @FXML private TableColumn<Vehicle, String> colorColumn;
    @FXML private TableColumn<Vehicle, String> engineColumn;
    @FXML private TableColumn<Vehicle, String> plateColumn;
    @FXML private TableColumn<Vehicle, Number> priceColumn;
    @FXML private TableColumn<Vehicle, Number> depositColumn;
    @FXML private TableColumn<Vehicle, String> statusColumn;

    @FXML private Label statusLabel;

    private final ViewVehicleViewModel viewModel = new ViewVehicleViewModel();

    @FXML
    public void initialize() {

        viewModel.color.bind(colorField.textProperty());
        viewModel.vehicleType.bind(vehicleTypeField.textProperty());
        viewModel.status.bind(statusField.textProperty());
        viewModel.maxPrice.bind(maxPriceField.textProperty().map(this::parseDoubleOrZero));

        viewModel.startDate.bind(startDateField.textProperty());
        viewModel.endDate.bind(endDateField.textProperty());
       
        vehicleIdColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getVehicleId()));

        modelColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getModel()));

        vehicleTypeColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getVehicleType()));

        colorColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getColor()));

        engineColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEngine()));

        plateColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPlateNo()));

        priceColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getPriceHour()));

        depositColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getDeposit()));

        statusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCurrentState()));

        vehicleTable.setItems(viewModel.vehicles);

        vehicleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        statusLabel.textProperty().bind(viewModel.statusMessage);

        viewModel.loadVehicles();
    }

    @FXML
    private void handleFilter() {
        viewModel.filterVehicles();
    }

    @FXML
    private void handleRefresh() {
        viewModel.loadVehicles();
    }

    @FXML
    private void handleCheckAvailability() {
        viewModel.checkAvailability();
    }

    private double parseDoubleOrZero(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0;
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

}