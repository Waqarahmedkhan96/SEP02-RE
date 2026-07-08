package view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Booking;
import viewmodel.BookingViewModel;

import java.time.LocalDateTime;
import java.util.Optional;

public class CancelBookingController {
    @FXML private VBox cancelRoot;
    @FXML private TableView<Booking> cancelBookingsTable;
    @FXML private TableColumn<Booking, Number> cancelBookingIdColumn;
    @FXML private TableColumn<Booking, String> cancelStartDateColumn;
    @FXML private TableColumn<Booking, String> cancelEndDateColumn;
    @FXML private TableColumn<Booking, String> cancelStatusColumn;
    @FXML private TableColumn<Booking, Number> cancelVehicleIdColumn;
    @FXML private TextField cancelSearchField;
    @FXML private Label cancelSelectionLabel;
    @FXML private Button confirmCancelBookingButton;
    @FXML private Label statusLabel;

    private final BookingViewModel viewModel = new BookingViewModel();

    @FXML
    public void initialize() {
        BookingTableBinder.wireBookingTable(cancelBookingsTable, cancelBookingIdColumn, cancelStartDateColumn,
                cancelEndDateColumn, cancelStatusColumn, cancelVehicleIdColumn);
        cancelBookingsTable.setItems(viewModel.cancellableBookings);
        cancelBookingsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldBooking, selectedBooking) ->
                updateCancelSelection(selectedBooking));
        statusLabel.textProperty().bind(viewModel.statusMessage);
        statusLabel.visibleProperty().bind(viewModel.statusMessage.isNotEmpty());
        statusLabel.managedProperty().bind(statusLabel.visibleProperty());
        handleSearchCancellableBookings();
    }

    @FXML
    private void showBookingMenu() {
        BookingNavigation.fire(cancelRoot, NavigationEvents.SHOW_BOOKING_MENU);
    }

    @FXML
    private void handleSearchCancellableBookings() {
        String query = cancelSearchField == null ? "" : cancelSearchField.getText();
        viewModel.searchCancellableBookings(query);
        cancelBookingsTable.getSelectionModel().clearSelection();
        updateCancelSelection(null);
    }

    @FXML
    private void handleCancelBooking() {
        Booking selectedBooking = cancelBookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            viewModel.statusMessage.set("Select a booking to cancel");
            return;
        }

        if (!selectedBooking.getStartDate().isAfter(LocalDateTime.now())) {
            viewModel.statusMessage.set("The booking period has already started, so the booking cannot be cancelled");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Cancel Booking");
        confirmation.setHeaderText("Cancel booking #" + selectedBooking.getBookingId() + "?");
        confirmation.setContentText("The booking will be marked as cancelled and the vehicle will be released for this period.");
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            viewModel.statusMessage.set("Cancellation process cancelled");
            return;
        }

        if (viewModel.cancelBooking(selectedBooking.getBookingId())) {
            handleSearchCancellableBookings();
        }
    }

    private void updateCancelSelection(Booking selectedBooking) {
        boolean hasSelection = selectedBooking != null;
        confirmCancelBookingButton.setDisable(!hasSelection);
        if (!hasSelection) {
            cancelSelectionLabel.setText("Select a booking to cancel");
            return;
        }

        cancelSelectionLabel.setText("Booking #" + selectedBooking.getBookingId()
                + " | Vehicle #" + selectedBooking.getVehicleId()
                + " | " + selectedBooking.getStartDate().toLocalDate()
                + " to " + selectedBooking.getEndDate().toLocalDate());
    }
}
