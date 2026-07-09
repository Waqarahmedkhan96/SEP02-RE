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
import shared.GetBookingsRequest;
import shared.GetBookingsResponse;

import java.util.Locale;

public class BookingHistoryController {
    @FXML private VBox historyRoot;
    @FXML private TextField historyCustomerField;
    @FXML private TextField historyVehicleField;
    @FXML private TextField historyStatusField;
    @FXML private TableView<Booking> historyBookingsTable;
    @FXML private TableColumn<Booking, Number> historyBookingIdColumn;
    @FXML private TableColumn<Booking, String> historyStartDateColumn;
    @FXML private TableColumn<Booking, String> historyEndDateColumn;
    @FXML private TableColumn<Booking, String> historyStatusColumn;
    @FXML private TableColumn<Booking, Number> historyVehicleIdColumn;
    @FXML private Label historyStatusLabel;

    private final Client client = new Client();
    private final ObservableList<Booking> bookings = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        BookingTableBinder.wireBookingTable(historyBookingsTable, historyBookingIdColumn, historyStartDateColumn,
                historyEndDateColumn, historyStatusColumn, historyVehicleIdColumn);
        historyBookingsTable.setItems(bookings);
        loadBookingsFromDatabase();
    }

    @FXML
    private void showBookingMenu() {
        BookingNavigation.fire(historyRoot, NavigationEvents.SHOW_BOOKING_MENU);
    }

    @FXML
    private void handleApplyFilters() {
        String customerQuery = normalize(historyCustomerField.getText());
        String vehicleQuery = normalize(historyVehicleField.getText());
        String statusQuery = normalize(historyStatusField.getText());

        ObservableList<Booking> filteredBookings = bookings.stream()
                .filter(booking -> customerQuery.isBlank()
                        || String.valueOf(booking.getCustomerId()).contains(customerQuery))
                .filter(booking -> vehicleQuery.isBlank()
                        || String.valueOf(booking.getVehicleId()).contains(vehicleQuery))
                .filter(booking -> statusQuery.isBlank()
                        || normalize(booking.getBookingStatus()).contains(statusQuery))
                .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);

        historyBookingsTable.setItems(filteredBookings);
        historyStatusLabel.setText("Found " + filteredBookings.size() + " booking(s)");
    }

    private void loadBookingsFromDatabase() {
        try {
            GetBookingsResponse response = client.getBookings(new GetBookingsRequest());
            if (response.isSuccess()) {
                bookings.setAll(response.getBookings());
                historyBookingsTable.setItems(bookings);
                historyStatusLabel.setText("Loaded " + bookings.size() + " booking(s) from database");
            } else {
                historyStatusLabel.setText("Failed: " + response.getMessage());
            }
        } catch (Exception e) {
            historyStatusLabel.setText("Connection error: " + e.getMessage());
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}
