package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Booking;
import viewmodel.BookingViewModel;

public class BookingHistoryController {
    @FXML private VBox historyRoot;
    @FXML private TableView<Booking> historyBookingsTable;
    @FXML private TableColumn<Booking, Number> historyBookingIdColumn;
    @FXML private TableColumn<Booking, String> historyStartDateColumn;
    @FXML private TableColumn<Booking, String> historyEndDateColumn;
    @FXML private TableColumn<Booking, String> historyStatusColumn;
    @FXML private TableColumn<Booking, Number> historyVehicleIdColumn;
    @FXML private TextField historyCustomerFilterField;
    @FXML private TextField historyVehicleFilterField;
    @FXML private TextField historyDateFilterField;
    @FXML private Label historyDetailsLabel;
    @FXML private Label statusLabel;

    private final BookingViewModel viewModel = new BookingViewModel();

    @FXML
    public void initialize() {
        BookingTableBinder.wireBookingTable(historyBookingsTable, historyBookingIdColumn, historyStartDateColumn,
                historyEndDateColumn, historyStatusColumn, historyVehicleIdColumn);
        historyBookingsTable.setItems(viewModel.archivedBookings);
        historyBookingsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldBooking, selectedBooking) ->
                showArchivedBookingDetails(selectedBooking));
        statusLabel.textProperty().bind(viewModel.statusMessage);
        statusLabel.visibleProperty().bind(viewModel.statusMessage.isNotEmpty());
        statusLabel.managedProperty().bind(statusLabel.visibleProperty());
        handleApplyHistoryFilters();
    }

    @FXML
    private void showBookingMenu() {
        BookingNavigation.fire(historyRoot, NavigationEvents.SHOW_BOOKING_MENU);
    }

    @FXML
    private void handleApplyHistoryFilters() {
        viewModel.loadArchivedBookings(
                historyCustomerFilterField.getText(),
                historyVehicleFilterField.getText(),
                historyDateFilterField.getText()
        );
        historyBookingsTable.getSelectionModel().clearSelection();
        historyDetailsLabel.setText("Select an archived booking to view details.");
    }

    @FXML
    private void handleClearHistoryFilters() {
        historyCustomerFilterField.clear();
        historyVehicleFilterField.clear();
        historyDateFilterField.clear();
        handleApplyHistoryFilters();
    }

    private void showArchivedBookingDetails(Booking booking) {
        if (booking == null) {
            return;
        }

        String actualReturn = booking.getActualReturnDate() == null
                ? "Not recorded"
                : String.valueOf(booking.getActualReturnDate());
        historyDetailsLabel.setText("Booking #" + booking.getBookingId()
                + " | Customer ID: " + booking.getCustomerId()
                + " | Vehicle ID: " + booking.getVehicleId()
                + " | Employee ID: " + booking.getEmployeeId()
                + " | Start: " + booking.getStartDate()
                + " | End: " + booking.getEndDate()
                + " | Returned: " + actualReturn
                + " | Status: " + booking.getBookingStatus());
    }
}
