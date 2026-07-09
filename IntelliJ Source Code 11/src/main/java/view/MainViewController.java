package view;

import client.Client;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.Booking;
import model.Vehicle;
import shared.GetVehiclesRequest;
import shared.SearchBookingsRequest;

import java.time.LocalDate;
import java.util.List;

public class MainViewController {

    @FXML private BorderPane appRoot;
    @FXML private Label breadcrumbLabel;
    @FXML private Label activeBookingsValueLabel;
    @FXML private Label availableVehiclesValueLabel;
    @FXML private Label overdueReturnsValueLabel;
    @FXML private Label completedTodayValueLabel;
    @FXML private VBox dashboardPage;
    @FXML private VBox customerPage;
    @FXML private VBox vehiclePage;
    @FXML private VBox bookingPage;

    private final Client client = new Client();

    @FXML
    public void initialize() {
        appRoot.addEventHandler(NavigationEvents.SHOW_DASHBOARD, event -> {
            showPage(dashboardPage, "Employee Start / Dashboard");
            loadDashboardData();
            event.consume();
        });
        bookingPage.addEventHandler(NavigationEvents.SHOW_DASHBOARD, event -> {
            showPage(dashboardPage, "Employee Start / Dashboard");
            loadDashboardData();
            event.consume();
        });
        loadDashboardData();
        addButtonAnimations(appRoot);
    }

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
        setPageVisible(dashboardPage, selectedPage == dashboardPage);
        setPageVisible(customerPage, selectedPage == customerPage);
        setPageVisible(vehiclePage, selectedPage == vehiclePage);
        setPageVisible(bookingPage, selectedPage == bookingPage);
        breadcrumbLabel.setText(breadcrumb);
    }

    private void setPageVisible(VBox page, boolean visible) {
        page.setVisible(visible);
        page.setManaged(visible);
    }

    private void loadDashboardData() {
        try {
            List<Booking> bookings = client.searchBookings(new SearchBookingsRequest("", false)).getBookings();
            List<Vehicle> vehicles = client.getVehicles(new GetVehiclesRequest()).getVehicles();
            LocalDate today = LocalDate.now();

            activeBookingsValueLabel.setText(String.valueOf(countBookingsByStatus(bookings, "ACTIVE")));
            availableVehiclesValueLabel.setText(String.valueOf(vehicles.stream()
                    .filter(vehicle -> "available".equalsIgnoreCase(vehicle.getCurrentState()))
                    .count()));
            overdueReturnsValueLabel.setText(String.valueOf(countBookingsByStatus(bookings, "OVERDUE")));
            completedTodayValueLabel.setText(String.valueOf(bookings.stream()
                    .filter(booking -> "COMPLETED".equalsIgnoreCase(booking.getBookingStatus()))
                    .filter(booking -> booking.getActualReturnDate() != null)
                    .filter(booking -> today.equals(booking.getActualReturnDate().toLocalDate()))
                    .count()));
        } catch (Exception e) {
            activeBookingsValueLabel.setText("0");
            availableVehiclesValueLabel.setText("0");
            overdueReturnsValueLabel.setText("0");
            completedTodayValueLabel.setText("0");
        }
    }

    private long countBookingsByStatus(List<Booking> bookings, String status) {
        return bookings.stream()
                .filter(booking -> status.equalsIgnoreCase(booking.getBookingStatus()))
                .count();
    }

    private void addButtonAnimations(Node root) {
        root.lookupAll(".button").forEach(button -> {
            button.setOnMouseEntered(event -> animateButton(button, 1.04));
            button.setOnMouseExited(event -> animateButton(button, 1.0));
            button.setOnMousePressed(event -> animateButton(button, 0.97));
            button.setOnMouseReleased(event -> animateButton(button, 1.04));
        });
    }

    private void animateButton(Node button, double scale) {
        ScaleTransition transition = new ScaleTransition(Duration.millis(110), button);
        transition.setToX(scale);
        transition.setToY(scale);
        transition.play();
    }
}
