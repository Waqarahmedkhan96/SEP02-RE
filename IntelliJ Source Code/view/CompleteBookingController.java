package view;

import client.Client;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Booking;
import shared.CompleteBookingRequest;
import shared.CompleteBookingResponse;
import shared.GetBookingsRequest;
import shared.GetBookingsResponse;

import java.util.Locale;

public class CompleteBookingController {
    @FXML private VBox completeRoot;
    @FXML private TextField completeSearchField;
    @FXML private TableView<Booking> completeBookingsTable;
    @FXML private TableColumn<Booking, Number> completeBookingIdColumn;
    @FXML private TableColumn<Booking, String> completeStartDateColumn;
    @FXML private TableColumn<Booking, String> completeEndDateColumn;
    @FXML private TableColumn<Booking, String> completeActualReturnColumn;
    @FXML private TableColumn<Booking, String> completeStatusColumn;
    @FXML private TableColumn<Booking, Number> completeVehicleIdColumn;
    @FXML private Label completeStatusLabel;

    private final Client client = new Client();
    private final ObservableList<Booking> bookings = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        BookingTableBinder.wireBookingTable(completeBookingsTable, completeBookingIdColumn, completeStartDateColumn,
                completeEndDateColumn, completeStatusColumn, completeVehicleIdColumn);

        completeActualReturnColumn.setCellValueFactory(data -> {
            var actualReturnDate = data.getValue().getActualReturnDate();
            return new javafx.beans.property.SimpleStringProperty(
                    actualReturnDate == null ? "—" : actualReturnDate.toString());
        });

        completeBookingsTable.setItems(bookings);
        loadBookingsFromDatabase();
    }

    @FXML
    private void showBookingMenu() {
        BookingNavigation.fire(completeRoot, NavigationEvents.SHOW_BOOKING_MENU);
    }

    @FXML
    private void handleSearchBookings() {
        String query = normalize(completeSearchField.getText());
        if (query.isBlank()) {
            completeBookingsTable.setItems(bookings);
            completeStatusLabel.setText("Showing " + bookings.size() + " booking(s)");
            return;
        }

        ObservableList<Booking> filteredBookings = bookings.stream()
                .filter(booking -> matchesBooking(booking, query))
                .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);

        completeBookingsTable.setItems(filteredBookings);
        completeStatusLabel.setText("Found " + filteredBookings.size() + " booking(s)");
    }

    @FXML
    private void handleCompleteSelectedBooking() {
        Booking selectedBooking = completeBookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            completeStatusLabel.setText("Select a booking to complete.");
            return;
        }

        try {
            CompleteBookingResponse response = client.completeBooking(new CompleteBookingRequest(selectedBooking.getBookingId()));
            String completionMessage = response.getMessage();

            if (response.isSuccess()) {
                loadBookingsFromDatabase();
                handleSearchBookings();
            }

            completeStatusLabel.setText(completionMessage); // set last, so it isn't overwritten
        } catch (Exception e) {
            completeStatusLabel.setText("Connection error: " + e.getMessage());
        }
    }

    private void loadBookingsFromDatabase() {
        try {
            GetBookingsResponse response = client.getBookings(new GetBookingsRequest());
            if (response.isSuccess()) {
                List<Booking> completableBookings = response.getBookings().stream()
                        .filter(booking -> {
                            String status = booking.getBookingStatus();
                            return !"COMPLETED".equalsIgnoreCase(status) && !"CANCELLED".equalsIgnoreCase(status);
                        })
                        .toList();
                bookings.setAll(completableBookings);
                completeBookingsTable.setItems(bookings);
                completeStatusLabel.setText("Loaded " + bookings.size() + " booking(s) from database");
            } else {
                completeStatusLabel.setText("Failed: " + response.getMessage());
            }
        } catch (Exception e) {
            completeStatusLabel.setText("Connection error: " + e.getMessage());
        }
    }

    private boolean matchesBooking(Booking booking, String query) {
        return String.valueOf(booking.getBookingId()).contains(query)
                || String.valueOf(booking.getCustomerId()).contains(query)
                || String.valueOf(booking.getVehicleId()).contains(query)
                || normalize(booking.getBookingStatus()).contains(query);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}
