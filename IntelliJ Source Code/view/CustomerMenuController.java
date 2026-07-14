package view;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class CustomerMenuController {

    @FXML private VBox menuRoot;

    @FXML
    private void handleBackToDashboard() {
        menuRoot.fireEvent(new javafx.event.Event(NavigationEvents.SHOW_DASHBOARD));
    }

    @FXML
    private void showCreateCustomer() {
        menuRoot.fireEvent(new javafx.event.Event(NavigationEvents.SHOW_CREATE_CUSTOMER));
    }

    @FXML
    private void showUpdateCustomer() {
        menuRoot.fireEvent(new javafx.event.Event(NavigationEvents.SHOW_UPDATE_CUSTOMER));
    }
}
