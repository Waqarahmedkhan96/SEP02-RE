package view;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class BookingMenuController {
    @FXML private VBox menuRoot;

    @FXML
    private void handleBackToDashboard() {
        BookingNavigation.fire(menuRoot, NavigationEvents.SHOW_DASHBOARD);
    }

    @FXML
    private void showCreateBooking() {
        BookingNavigation.fire(menuRoot, NavigationEvents.SHOW_CREATE_BOOKING);
    }

    @FXML
    private void showUpdateBooking() {
        BookingNavigation.fire(menuRoot, NavigationEvents.SHOW_UPDATE_BOOKING);
    }

    @FXML
    private void showCancelBooking() {
        BookingNavigation.fire(menuRoot, NavigationEvents.SHOW_CANCEL_BOOKING);
    }

    @FXML
    private void showCompleteBooking() {
        BookingNavigation.fire(menuRoot, NavigationEvents.SHOW_COMPLETE_BOOKING);
    }

    @FXML
    private void showBookingHistory() {
        BookingNavigation.fire(menuRoot, NavigationEvents.SHOW_BOOKING_HISTORY);
    }
}
