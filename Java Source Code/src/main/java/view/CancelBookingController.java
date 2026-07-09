package view;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Booking;
import shared.CancelBookingRequest;
import shared.CancelBookingResponse;
import shared.GetBookingsRequest;
import shared.GetBookingsResponse;

import java.util.Locale;

public class CancelBookingController {
    @FXML private VBox cancelRoot;
    @FXML private TextField cancelSearchField;
    @FXML private TableView<Booking> cancelBookingsTable;
    @FXML private TableColumn<Booking, Number> cancelBookingIdColumn;
    @FXML private TableColumn<Booking, String> cancelStartDateColumn;
    @FXML private TableColumn<Booking, String> cancelEndDateColumn;
    @FXML private TableColumn<Booking, String> cancelStatusColumn;
    @FXML private TableColumn<Booking, Number> cancelVehicleIdColumn;
    @FXML private Label cancelStatusLabel;

    private final Client client = new Client();
    private final ObservableList<Booking> bookings = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        BookingTableBinder.wireBookingTable(cancelBookingsTable, cancelBookingIdColumn, cancelStartDateColumn,
                cancelEndDateColumn, cancelStatusColumn, cancelVehicleIdColumn);
        cancelBookingsTable.setItems(bookings);
        loadBookingsFromDatabase();
    }

    @FXML
    private void showBookingMenu() {
        BookingNavigation.fire(cancelRoot, NavigationEvents.SHOW_BOOKING_MENU);
    }

    @FXML
    private void handleFindBooking() {
        loadBookingsFromDatabase(false);
        applyCurrentFilter(null);
    }

    @FXML
    private void handleCancelSelectedBooking() {
        Booking selectedBooking = cancelBookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            cancelStatusLabel.setText("Select a booking to cancel.");
            return;
        }

        int bookingId = selectedBooking.getBookingId();
        try {
            CancelBookingResponse response = client.cancelBooking(new CancelBookingRequest(bookingId));
            if (response.isSuccess()) {
                loadBookingsFromDatabase(false);
                applyCurrentFilter("Cancelled booking #" + bookingId);
                selectBookingById(bookingId);
            } else {
                cancelStatusLabel.setText("Failed: " + response.getMessage());
            }
        } catch (Exception e) {
            cancelStatusLabel.setText("Connection error: " + e.getMessage());
        }
    }

    private void applyCurrentFilter(String successMessage) {
        String query = normalize(cancelSearchField.getText());
        if (query.isBlank()) {
            cancelBookingsTable.setItems(bookings);
            cancelStatusLabel.setText(successMessage == null
                    ? "Showing " + bookings.size() + " booking(s)"
                    : successMessage);
            return;
        }

        ObservableList<Booking> filteredBookings = bookings.stream()
                .filter(booking -> matchesBooking(booking, query))
                .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);

        cancelBookingsTable.setItems(filteredBookings);
        cancelStatusLabel.setText(successMessage == null
                ? "Found " + filteredBookings.size() + " booking(s)"
                : successMessage);
    }

    private void loadBookingsFromDatabase() {
        loadBookingsFromDatabase(true);
    }

    private void loadBookingsFromDatabase(boolean updateStatus) {
        try {
            GetBookingsResponse response = client.getBookings(new GetBookingsRequest());
            if (response.isSuccess()) {
                ObservableList<Booking> cancellableBookings = response.getBookings().stream()
                        .filter(this::canBeCancelled)
                        .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);
                bookings.setAll(cancellableBookings);
                cancelBookingsTable.setItems(bookings);
                if (updateStatus) {
                    cancelStatusLabel.setText("Loaded " + bookings.size() + " cancellable booking(s) from database");
                }
            } else {
                cancelStatusLabel.setText("Failed: " + response.getMessage());
            }
        } catch (Exception e) {
            cancelStatusLabel.setText("Connection error: " + e.getMessage());
        }
    }

    private void selectBookingById(int bookingId) {
        for (Booking booking : cancelBookingsTable.getItems()) {
            if (booking.getBookingId() == bookingId) {
                cancelBookingsTable.getSelectionModel().select(booking);
                cancelBookingsTable.scrollTo(booking);
                return;
            }
        }
    }

    private boolean matchesBooking(Booking booking, String query) {
        return String.valueOf(booking.getBookingId()).contains(query)
                || String.valueOf(booking.getCustomerId()).contains(query)
                || String.valueOf(booking.getVehicleId()).contains(query)
                || normalize(booking.getBookingStatus()).contains(query);
    }

    private boolean canBeCancelled(Booking booking) {
        String status = normalize(booking.getBookingStatus());
        return "active".equals(status) || "pending".equals(status);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}
