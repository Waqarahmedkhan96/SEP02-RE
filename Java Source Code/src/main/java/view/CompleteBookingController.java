package view;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import model.Booking;
import viewmodel.BookingViewModel;

public class CompleteBookingController {
    @FXML private VBox completeRoot;
    @FXML private TableView<Booking> completeBookingsTable;
    @FXML private TableColumn<Booking, Number> completeBookingIdColumn;
    @FXML private TableColumn<Booking, String> completeStartDateColumn;
    @FXML private TableColumn<Booking, String> completeEndDateColumn;
    @FXML private TableColumn<Booking, String> completeStatusColumn;
    @FXML private TableColumn<Booking, Number> completeVehicleIdColumn;

    private final BookingViewModel viewModel = new BookingViewModel();

    @FXML
    public void initialize() {
        BookingTableBinder.wireBookingTable(completeBookingsTable, completeBookingIdColumn, completeStartDateColumn,
                completeEndDateColumn, completeStatusColumn, completeVehicleIdColumn);
        completeBookingsTable.setItems(viewModel.customerBookings);
        viewModel.searchActiveBookings("");
    }

    @FXML
    private void showBookingMenu() {
        BookingNavigation.fire(completeRoot, NavigationEvents.SHOW_BOOKING_MENU);
    }
}
