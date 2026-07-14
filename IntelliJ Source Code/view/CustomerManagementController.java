package view;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class CustomerManagementController {

    @FXML private StackPane customerManagementRoot;
    @FXML private VBox menuPage;
    @FXML private VBox createPage;
    @FXML private VBox updatePage;
    @FXML private UpdateCustomerController updateCustomerController;

    @FXML
    public void initialize() {
        customerManagementRoot.addEventHandler(NavigationEvents.SHOW_CUSTOMER_MENU, event -> {
            showPage(menuPage);
            event.consume();
        });
        customerManagementRoot.addEventHandler(NavigationEvents.SHOW_CREATE_CUSTOMER, event -> {
            showPage(createPage);
            event.consume();
        });
        customerManagementRoot.addEventHandler(NavigationEvents.SHOW_UPDATE_CUSTOMER, event -> {
            showPage(updatePage);
            event.consume();
        });
        showPage(menuPage);
    }

    private void showPage(VBox selectedPage) {
        setPageVisible(menuPage, selectedPage == menuPage);
        setPageVisible(createPage, selectedPage == createPage);
        setPageVisible(updatePage, selectedPage == updatePage);
        if (selectedPage == updatePage) {
            updateCustomerController.refreshCustomers();
        }
    }

    private void setPageVisible(VBox page, boolean visible) {
        page.setVisible(visible);
        page.setManaged(visible);
    }
}
