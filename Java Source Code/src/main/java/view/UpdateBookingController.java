package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import mockdata.MockBookings;
import model.Booking;
import viewmodel.BookingViewModel;

public class UpdateBookingController {
    @FXML private VBox updateRoot;
    @FXML private TextField bookingIdField;
    @FXML private TextField customerIdField;
    @FXML private TextField vehicleIdField;
    @FXML private TextField updateVehicleIdField;
    @FXML private TextField employeeIdField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private DatePicker updateStartDatePicker;
    @FXML private DatePicker updateEndDatePicker;
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

        BookingTableBinder.wireBookingTable(bookingsTable, bookingIdColumn, startDateColumn, endDateColumn,
                statusColumn, vehicleIdColumn);
        bookingsTable.setItems(viewModel.customerBookings);
        bookingsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldBooking, selectedBooking) ->
                fillUpdateForm(selectedBooking));

        statusLabel.textProperty().bind(viewModel.statusMessage);
        statusLabel.visibleProperty().bind(viewModel.statusMessage.isNotEmpty());
        statusLabel.managedProperty().bind(statusLabel.visibleProperty());
        showMockActiveBookings();
    }

    @FXML
    private void showBookingMenu() {
        BookingNavigation.fire(updateRoot, NavigationEvents.SHOW_BOOKING_MENU);
    }

    @FXML
    private void handleSearchActiveBookings() {
        viewModel.loadActiveCustomerBookings();
        if (viewModel.customerBookings.isEmpty()) {
            showMockActiveBookings();
        }
    }

    @FXML
    private void handleUpdateBooking() {
        copyUpdateFieldsToBoundCreateFields();
        if (viewModel.updateBooking()) {
            viewModel.loadActiveCustomerBookings();
        }
    }

    private void showMockActiveBookings() {
        ObservableList<Booking> activeBookings = MockBookings.getBookings().stream()
                .filter(booking -> "ACTIVE".equalsIgnoreCase(booking.getBookingStatus()))
                .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);
        viewModel.customerBookings.setAll(activeBookings);
        viewModel.statusMessage.set("Showing " + activeBookings.size() + " mocked active booking(s)");
    }

    private void fillUpdateForm(Booking selectedBooking) {
        if (selectedBooking == null) {
            return;
        }

        bookingIdField.setText(String.valueOf(selectedBooking.getBookingId()));
        customerIdField.setText(String.valueOf(selectedBooking.getCustomerId()));
        vehicleIdField.setText(String.valueOf(selectedBooking.getVehicleId()));
        updateVehicleIdField.setText(String.valueOf(selectedBooking.getVehicleId()));
        employeeIdField.setText(String.valueOf(selectedBooking.getEmployeeId()));
        startDatePicker.setValue(selectedBooking.getStartDate().toLocalDate());
        endDatePicker.setValue(selectedBooking.getEndDate().toLocalDate());
        updateStartDatePicker.setValue(selectedBooking.getStartDate().toLocalDate());
        updateEndDatePicker.setValue(selectedBooking.getEndDate().toLocalDate());
        statusField.setText(selectedBooking.getBookingStatus());
    }

    private void copyUpdateFieldsToBoundCreateFields() {
        vehicleIdField.setText(updateVehicleIdField.getText());
        startDatePicker.setValue(updateStartDatePicker.getValue());
        endDatePicker.setValue(updateEndDatePicker.getValue());
        statusField.setText("ACTIVE");
    }

    private int parseIntOrZero(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
