package view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import mockdata.MockBookings;
import model.Booking;

public class BookingHistoryController {
    @FXML private VBox historyRoot;
    @FXML private TableView<Booking> historyBookingsTable;
    @FXML private TableColumn<Booking, Number> historyBookingIdColumn;
    @FXML private TableColumn<Booking, String> historyStartDateColumn;
    @FXML private TableColumn<Booking, String> historyEndDateColumn;
    @FXML private TableColumn<Booking, String> historyStatusColumn;
    @FXML private TableColumn<Booking, Number> historyVehicleIdColumn;

    @FXML
    public void initialize() {
        BookingTableBinder.wireBookingTable(historyBookingsTable, historyBookingIdColumn, historyStartDateColumn,
                historyEndDateColumn, historyStatusColumn, historyVehicleIdColumn);
        historyBookingsTable.setItems(FXCollections.observableArrayList(MockBookings.getBookings()));
    }

    @FXML
    private void showBookingMenu() {
        BookingNavigation.fire(historyRoot, NavigationEvents.SHOW_BOOKING_MENU);
    }
}
