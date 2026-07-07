package view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Booking;
import viewmodel.BookingViewModel;

public class BookingViewController {

    @FXML private TextField customerIdField;
    @FXML private TextField bookingIdField;
    @FXML private TextField vehicleIdField;
    @FXML private TextField employeeIdField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField statusField;
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
        viewModel.bookingId.bind(bookingIdField.textProperty().map(this::parseIntOrZero));
        viewModel.vehicleId.bind(vehicleIdField.textProperty().map(this::parseIntOrZero));
        viewModel.employeeId.bind(employeeIdField.textProperty().map(this::parseIntOrZero));
        viewModel.startDate.bind(startDatePicker.valueProperty().map(String::valueOf));
        viewModel.endDate.bind(endDatePicker.valueProperty().map(String::valueOf));
        viewModel.bookingStatus.bind(statusField.textProperty());

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
        bookingsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldBooking, selectedBooking) -> {
            if (selectedBooking != null) {
                bookingIdField.setText(String.valueOf(selectedBooking.getBookingId()));
                customerIdField.setText(String.valueOf(selectedBooking.getCustomerId()));
                vehicleIdField.setText(String.valueOf(selectedBooking.getVehicleId()));
                employeeIdField.setText(String.valueOf(selectedBooking.getEmployeeId()));
                startDatePicker.setValue(selectedBooking.getStartDate().toLocalDate());
                endDatePicker.setValue(selectedBooking.getEndDate().toLocalDate());
                statusField.setText(selectedBooking.getBookingStatus());
            }
        });
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

    @FXML
    private void handleSearchActiveBookings() {
        viewModel.loadActiveCustomerBookings();
    }

    @FXML
    private void handleUpdateBooking() {
        if (viewModel.updateBooking()) {
            viewModel.loadActiveCustomerBookings();
        }
    }

    private int parseIntOrZero(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
