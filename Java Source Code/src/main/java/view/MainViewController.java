package view;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class MainViewController {

    @FXML private Label breadcrumbLabel;
    @FXML private VBox dashboardPage;
    @FXML private VBox customerPage;
    @FXML private VBox vehiclePage;
    @FXML private VBox bookingPage;

    @FXML
    private void showCustomers() {
        showPage(customerPage, "Employee Start / Customers");
    }

    @FXML
    private void showVehicles() {
        showPage(vehiclePage, "Employee Start / Vehicles");
    }

    @FXML
    private void showBookings() {
        showPage(bookingPage, "Employee Start / Bookings");
    }

    private void showPage(VBox selectedPage, String breadcrumb) {
        setPageVisible(dashboardPage, false);
        setPageVisible(customerPage, selectedPage == customerPage);
        setPageVisible(vehiclePage, selectedPage == vehiclePage);
        setPageVisible(bookingPage, selectedPage == bookingPage);
        breadcrumbLabel.setText(breadcrumb);
    }

    private void setPageVisible(VBox page, boolean visible) {
        page.setVisible(visible);
        page.setManaged(visible);
    }
}
