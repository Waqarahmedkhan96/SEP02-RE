package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Booking;
import viewmodel.BookingViewModel;

public class CompleteBookingController {
    @FXML private VBox completeRoot;
    @FXML private TextField searchField;
    @FXML private TableView<Booking> completeBookingsTable;
    @FXML private TableColumn<Booking, Number> completeBookingIdColumn;
    @FXML private TableColumn<Booking, String> completeStartDateColumn;
    @FXML private TableColumn<Booking, String> completeEndDateColumn;
    @FXML private TableColumn<Booking, String> completeStatusColumn;
    @FXML private TableColumn<Booking, Number> completeVehicleIdColumn;
    @FXML private Label selectedBookingLabel;
    @FXML private Button completeButton;
    @FXML private Label resultLabel;

    private final BookingViewModel viewModel = new BookingViewModel();
    private Booking selectedBooking;

    @FXML
    public void initialize() {
        BookingTableBinder.wireBookingTable(completeBookingsTable, completeBookingIdColumn, completeStartDateColumn,
                completeEndDateColumn, completeStatusColumn, completeVehicleIdColumn);
        completeBookingsTable.setItems(viewModel.customerBookings);
        viewModel.searchActiveBookings("");

        completeBookingsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedBooking = newVal;
            if (newVal != null) {
                selectedBookingLabel.setText("Selected booking #" + newVal.getBookingId()
                        + " (vehicle " + newVal.getVehicleId() + ", status " + newVal.getBookingStatus() + ")");
                completeButton.setDisable(false);
            } else {
                selectedBookingLabel.setText("Select a booking above to complete it");
                completeButton.setDisable(true);
            }
        });
    }

    @FXML
    private void handleSearch() {
        viewModel.searchActiveBookings(searchField.getText());
        resultLabel.setText(viewModel.statusMessage.get());
    }

    @FXML
    private void handleComplete() {
        if (selectedBooking == null) return;

        boolean success = viewModel.completeBooking(selectedBooking.getBookingId());
        resultLabel.setText(viewModel.statusMessage.get());

        if (success) {
            completeButton.setDisable(true);
            selectedBooking = null;
            selectedBookingLabel.setText("Select a booking above to complete it");
            viewModel.searchActiveBookings(searchField.getText() == null ? "" : searchField.getText());
        }
    }

    @FXML
    private void showBookingMenu() {
        BookingNavigation.fire(completeRoot, NavigationEvents.SHOW_BOOKING_MENU);
    }
}