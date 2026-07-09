package view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Customer;
import model.Vehicle;
import viewmodel.BookingViewModel;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class CreateBookingController {
    @FXML private VBox createRoot;
    @FXML private TextField customerIdField;
    @FXML private ComboBox<String> customerComboBox;
    @FXML private TextField vehicleIdField;
    @FXML private TextField employeeIdField;
    @FXML private ComboBox<String> employeeIdComboBox;
    @FXML private TextField colorFilterField;
    @FXML private TextField vehicleTypeFilterField;
    @FXML private TextField maxPriceFilterField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField statusField;
    @FXML private TableView<Vehicle> createVehiclesTable;
    @FXML private TableColumn<Vehicle, Number> createVehicleIdColumn;
    @FXML private TableColumn<Vehicle, String> createVehicleModelColumn;
    @FXML private TableColumn<Vehicle, String> createVehicleTypeColumn;
    @FXML private TableColumn<Vehicle, String> createVehicleColorColumn;
    @FXML private TableColumn<Vehicle, Number> createVehiclePriceColumn;
    @FXML private TableColumn<Vehicle, String> createVehicleStatusColumn;
    @FXML private Label selectedVehicleStatusLabel;
    @FXML private Button confirmBookingButton;
    @FXML private Label statusLabel;

    private final BookingViewModel viewModel = new BookingViewModel();

    @FXML
    public void initialize() {
        viewModel.customerId.bind(customerIdField.textProperty().map(this::parseIntOrZero));
        viewModel.vehicleId.bind(vehicleIdField.textProperty().map(this::parseIntOrZero));
        viewModel.employeeId.bind(employeeIdField.textProperty().map(this::parseIntOrZero));
        viewModel.startDate.bind(startDatePicker.valueProperty().map(String::valueOf));
        viewModel.endDate.bind(endDatePicker.valueProperty().map(String::valueOf));
        viewModel.bookingStatus.bind(statusField.textProperty());

        wireVehicleTable();
        setupCreateBookingSelectors();
        createVehiclesTable.setItems(viewModel.availableVehicles);
        createVehiclesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVehicle, selectedVehicle) -> {
            if (selectedVehicle != null) {
                vehicleIdField.setText(String.valueOf(selectedVehicle.getVehicleId()));
                updateSelectedVehicleStatus(selectedVehicle);
            }
        });

        statusLabel.textProperty().bind(viewModel.statusMessage);
        statusLabel.visibleProperty().bind(viewModel.statusMessage.isNotEmpty());
        statusLabel.managedProperty().bind(statusLabel.visibleProperty());
        startDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> loadAvailableVehiclesIfReady());
        endDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> loadAvailableVehiclesIfReady());
        viewModel.loadCustomers();
        loadAvailableVehiclesIfReady();
    }

    @FXML
    private void showBookingMenu() {
        BookingNavigation.fire(createRoot, NavigationEvents.SHOW_BOOKING_MENU);
    }

    @FXML
    private void handleCreateBooking() {
        if (!isCreateFormValid()) {
            return;
        }
        Vehicle selectedVehicle = createVehiclesTable.getSelectionModel().getSelectedItem();
        int bookingId = viewModel.submit();
        if (bookingId > 0) {
            showBookingCreatedConfirmation(bookingId, selectedVehicle);
        }
        handleShowAvailableVehicles();
    }

    @FXML
    private void handleShowAvailableVehicles() {
        String color = normalize(colorFilterField.getText());
        String type = normalize(vehicleTypeFilterField.getText());
        double maxPrice = parseDoubleOrZero(maxPriceFilterField.getText());

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        if (startDate == null || endDate == null) {
            viewModel.statusMessage.set("Select start and end dates before loading vehicles");
            return;
        }
        if (!startDate.isBefore(endDate)) {
            viewModel.statusMessage.set("Start date must be before end date");
            return;
        }

        viewModel.loadAvailableVehicles(String.valueOf(startDate), String.valueOf(endDate));
        ObservableList<Vehicle> filteredVehicles = viewModel.availableVehicles.stream()
                .filter(vehicle -> color.isBlank() || contains(vehicle.getColor(), color))
                .filter(vehicle -> type.isBlank() || contains(vehicle.getVehicleType(), type))
                .filter(vehicle -> maxPrice <= 0 || vehicle.getPriceHour() <= maxPrice)
                .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);

        createVehiclesTable.setItems(filteredVehicles);
        createVehiclesTable.getSelectionModel().clearSelection();
        vehicleIdField.clear();
        resetSelectedVehicleStatus();
        viewModel.statusMessage.set("Showing " + filteredVehicles.size() + " vehicle(s) for the selected period");
    }

    private void loadAvailableVehiclesIfReady() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        if (startDate != null && endDate != null && startDate.isBefore(endDate)) {
            handleShowAvailableVehicles();
        }
    }

    private void wireVehicleTable() {
        createVehicleIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getVehicleId()));
        createVehicleModelColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getModel()));
        createVehicleTypeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVehicleType()));
        createVehicleColorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getColor()));
        createVehiclePriceColumn.setCellValueFactory(data -> new SimpleIntegerProperty((int) data.getValue().getPriceHour()));
        createVehicleStatusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCurrentState()));
        createVehiclesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void setupCreateBookingSelectors() {
        ObservableList<String> customers = viewModel.customers.stream()
                .map(this::formatCustomerOption)
                .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);
        customerComboBox.setItems(customers);
        viewModel.customers.addListener((javafx.collections.ListChangeListener<Customer>) change ->
                customerComboBox.setItems(viewModel.customers.stream()
                        .map(this::formatCustomerOption)
                        .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll)));
        customerComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selectedValue) ->
                customerIdField.setText(String.valueOf(parseLeadingIntOrZero(selectedValue))));

        employeeIdComboBox.setItems(FXCollections.observableArrayList("999"));
        employeeIdComboBox.getSelectionModel().select("999");
        employeeIdField.setText("999");
        employeeIdComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selectedValue) ->
                employeeIdField.setText(selectedValue == null ? "" : selectedValue));
    }

    private void updateSelectedVehicleStatus(Vehicle selectedVehicle) {
        boolean available = "available".equalsIgnoreCase(selectedVehicle.getCurrentState());
        selectedVehicleStatusLabel.setText(available ? "Available" : selectedVehicle.getCurrentState());
        selectedVehicleStatusLabel.setStyle(available
                ? "-fx-background-color: #d9f3e3; -fx-text-fill: #159447; -fx-background-radius: 16; -fx-padding: 7 20 7 20; -fx-font-weight: bold;"
                : "-fx-background-color: #f8d7da; -fx-text-fill: #b00020; -fx-background-radius: 16; -fx-padding: 7 20 7 20; -fx-font-weight: bold;");
        confirmBookingButton.setDisable(!available);
        if (!available) {
            viewModel.statusMessage.set("Selected vehicle is " + selectedVehicle.getCurrentState() + " and cannot be booked");
        }
    }

    private void resetSelectedVehicleStatus() {
        selectedVehicleStatusLabel.setText("Select vehicle");
        selectedVehicleStatusLabel.setStyle("-fx-background-color: #eeeeee; -fx-text-fill: #777777; -fx-background-radius: 16; -fx-padding: 7 20 7 20; -fx-font-weight: bold;");
        confirmBookingButton.setDisable(false);
    }

    private boolean isCreateFormValid() {
        Vehicle selectedVehicle = createVehiclesTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            viewModel.statusMessage.set("Select an available vehicle first");
            return false;
        }
        if (!"available".equalsIgnoreCase(selectedVehicle.getCurrentState())) {
            viewModel.statusMessage.set("Selected vehicle is " + selectedVehicle.getCurrentState() + " and cannot be booked");
            return false;
        }
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        if (startDate == null || endDate == null) {
            viewModel.statusMessage.set("Select start and end dates");
            return false;
        }
        if (!startDate.isBefore(endDate)) {
            viewModel.statusMessage.set("Start date must be before end date");
            return false;
        }
        if (customerIdField.getText().isBlank() || employeeIdField.getText().isBlank()) {
            viewModel.statusMessage.set("Select a customer and employee");
            return false;
        }
        return true;
    }

    private void showBookingCreatedConfirmation(int bookingId, Vehicle vehicle) {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        long rentalDays = ChronoUnit.DAYS.between(startDate, endDate);
        double estimatedPrice = rentalDays * vehicle.getPriceHour();

        Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
        confirmation.setTitle("Booking Confirmed");
        confirmation.setHeaderText("Booking #" + bookingId + " created successfully");
        confirmation.setContentText(
                "Customer: " + customerComboBox.getValue() + System.lineSeparator() +
                "Vehicle: " + vehicle.getModel() + " (#" + vehicle.getVehicleId() + ")" + System.lineSeparator() +
                "Type: " + vehicle.getVehicleType() + ", " + vehicle.getColor() + System.lineSeparator() +
                "Rental period: " + startDate + " to " + endDate + System.lineSeparator() +
                "Price/hour: " + vehicle.getPriceHour() + System.lineSeparator() +
                "Estimated total: " + estimatedPrice + System.lineSeparator() +
                "Status: reserved"
        );
        confirmation.getButtonTypes().setAll(ButtonType.CLOSE);
        confirmation.showAndWait();
    }

    private int parseIntOrZero(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parseDoubleOrZero(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private boolean contains(String value, String filter) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(filter);
    }

    private String formatCustomerOption(Customer customer) {
        return customer.getCustomerId() + " - " + customer.getName();
    }

    private int parseLeadingIntOrZero(String value) {
        if (value == null) {
            return 0;
        }
        String id = value.split("-", 2)[0].trim();
        return parseIntOrZero(id);
    }
}
