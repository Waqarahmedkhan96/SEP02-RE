package view;

import client.Client;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Booking;
import shared.SearchBookingsRequest;
import shared.SearchBookingsResponse;

import java.util.Locale;

public class BookingHistoryController {
    @FXML private VBox historyRoot;
    @FXML private TextField historyBookingField;
    @FXML private TextField historyCustomerField;
    @FXML private TextField historyVehicleField;
    @FXML private TableView<Booking> historyBookingsTable;
    @FXML private TableColumn<Booking, Number> historyCustomerIdColumn;
    @FXML private TableColumn<Booking, Number> historyBookingIdColumn;
    @FXML private TableColumn<Booking, Number> historyVehicleIdColumn;
    @FXML private TableColumn<Booking, String> historyStartDateColumn;
    @FXML private TableColumn<Booking, String> historyEndDateColumn;
    @FXML private TableColumn<Booking, String> historyActualReturnDateColumn;
    @FXML private TableColumn<Booking, String> historyPriceHourColumn;
    @FXML private TableColumn<Booking, String> historyBookedHoursColumn;
    @FXML private TableColumn<Booking, String> historyActualHoursColumn;
    @FXML private TableColumn<Booking, String> historyLateHoursColumn;
    @FXML private TableColumn<Booking, String> historyTotalPriceColumn;
    @FXML private TableColumn<Booking, String> historyStatusColumn;
    @FXML private Label historyStatusLabel;

    private final Client client = new Client();
    private final ObservableList<Booking> bookings = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        BookingTableBinder.wireBookingTable(historyBookingsTable, historyBookingIdColumn, historyStartDateColumn,
                historyEndDateColumn, historyStatusColumn, historyVehicleIdColumn);
        historyCustomerIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCustomerId()));
        historyActualReturnDateColumn.setCellValueFactory(data -> new SimpleStringProperty(formatDate(data.getValue().getActualReturnDate())));
        historyPriceHourColumn.setCellValueFactory(data -> new SimpleStringProperty(formatMoney(data.getValue().getPriceHour())));
        historyBookedHoursColumn.setCellValueFactory(data -> new SimpleStringProperty(formatNumber(data.getValue().getBookedHours())));
        historyActualHoursColumn.setCellValueFactory(data -> new SimpleStringProperty(formatNumber(data.getValue().getActualHours())));
        historyLateHoursColumn.setCellValueFactory(data -> new SimpleStringProperty(formatNumber(data.getValue().getLateHours())));
        historyLateHoursColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(value);
                setStyle(parseNumber(value) > 0
                        ? "-fx-text-fill: #c8202f; -fx-font-weight: bold;"
                        : "-fx-text-fill: #20964b; -fx-font-weight: bold;");
            }
        });
        historyTotalPriceColumn.setCellValueFactory(data -> new SimpleStringProperty(formatMoney(data.getValue().getTotalPrice())));
        historyBookingsTable.setItems(bookings);
        loadBookingsFromDatabase();
    }

    @FXML
    private void showBookingMenu() {
        BookingNavigation.fire(historyRoot, NavigationEvents.SHOW_BOOKING_MENU);
    }

    @FXML
    private void handleApplyFilters() {
        String bookingQuery = normalize(historyBookingField.getText());
        String customerQuery = normalize(historyCustomerField.getText());
        String vehicleQuery = normalize(historyVehicleField.getText());
        loadArchivedBookingsFromDatabase(bookingQuery, customerQuery, vehicleQuery, "");
    }

    private void loadBookingsFromDatabase() {
        loadArchivedBookingsFromDatabase("", "", "", "");
    }

    private void loadArchivedBookingsFromDatabase(String bookingQuery, String customerQuery, String vehicleQuery, String dateQuery) {
        try {
            SearchBookingsResponse response = client.searchBookings(
                    new SearchBookingsRequest(bookingQuery, customerQuery, vehicleQuery, dateQuery, true));
            if (response.isSuccess()) {
                bookings.setAll(response.getBookings());
                historyBookingsTable.setItems(bookings);
                historyStatusLabel.setText("Loaded " + bookings.size() + " completed/cancelled booking(s) from database");
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

    private String formatMoney(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }

    private String formatNumber(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }

    private String formatDate(Object value) {
        return value == null ? "N/A" : value.toString();
    }

    private double parseNumber(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
