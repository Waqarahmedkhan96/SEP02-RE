package view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Booking;
import viewmodel.BookingViewModel;

public class BookingViewController {

    @FXML private TextField customerIdField;
    @FXML private TextField vehicleIdField;
    @FXML private TextField employeeIdField;
    @FXML private TextField startDateField;
    @FXML private TextField endDateField;
    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, Number> bookingIdColumn;
    @FXML private TableColumn<Booking, String> startDateColumn;
    @FXML private TableColumn<Booking, String> endDateColumn;
    @FXML private TableColumn<Booking, String> statusColumn;
    @FXML private TableColumn<Booking, Number> vehicleIdColumn;
    @FXML private Label statusLabel;

    private final BookingViewModel viewModel = new BookingViewModel();

    @FXML
    public void initialize() {
        viewModel.customerId.bind(customerIdField.textProperty().map(this::parseIntOrZero));
        viewModel.vehicleId.bind(vehicleIdField.textProperty().map(this::parseIntOrZero));
        viewModel.employeeId.bind(employeeIdField.textProperty().map(this::parseIntOrZero));
        viewModel.startDate.bind(startDateField.textProperty());
        viewModel.endDate.bind(endDateField.textProperty());

        bookingIdColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getBookingId()));
        startDateColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getStartDate())));
        endDateColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getEndDate())));
        statusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getBookingStatus()));
        vehicleIdColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getVehicleId()));

        bookingsTable.setItems(viewModel.customerBookings);
        statusLabel.textProperty().bind(viewModel.statusMessage);
    }

    @FXML
    private void handleCreateBooking() {
        viewModel.submit();
        viewModel.loadCustomerBookings();
    }

    @FXML
    private void handleLoadBookings() {
        viewModel.loadCustomerBookings();
    }

    private int parseIntOrZero(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
