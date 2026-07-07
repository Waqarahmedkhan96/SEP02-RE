package view;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class MainViewController {

    @FXML private BorderPane appRoot;
    @FXML private Label breadcrumbLabel;
    @FXML private VBox dashboardPage;
    @FXML private VBox customerPage;
    @FXML private VBox vehiclePage;
    @FXML private VBox bookingPage;

    @FXML
    public void initialize() {
        bookingPage.addEventHandler(NavigationEvents.SHOW_DASHBOARD, event -> {
            showPage(dashboardPage, "Employee Start / Dashboard");
            event.consume();
        });
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
