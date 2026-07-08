package view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import mockdata.MockBookings;
import model.Booking;

public class CancelBookingController {
    @FXML private VBox cancelRoot;
    @FXML private TableView<Booking> cancelBookingsTable;
    @FXML private TableColumn<Booking, Number> cancelBookingIdColumn;
    @FXML private TableColumn<Booking, String> cancelStartDateColumn;
    @FXML private TableColumn<Booking, String> cancelEndDateColumn;
    @FXML private TableColumn<Booking, String> cancelStatusColumn;
    @FXML private TableColumn<Booking, Number> cancelVehicleIdColumn;

    @FXML
    public void initialize() {
        BookingTableBinder.wireBookingTable(cancelBookingsTable, cancelBookingIdColumn, cancelStartDateColumn,
                cancelEndDateColumn, cancelStatusColumn, cancelVehicleIdColumn);
        cancelBookingsTable.setItems(FXCollections.observableArrayList(MockBookings.getBookings()));
    }

    @FXML
    private void showBookingMenu() {
        BookingNavigation.fire(cancelRoot, NavigationEvents.SHOW_BOOKING_MENU);
    }
}
