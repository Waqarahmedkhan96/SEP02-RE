package view;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class BookingViewController {

    @FXML private StackPane bookingRoot;
    @FXML private VBox menuPage;
    @FXML private VBox createPage;
    @FXML private VBox updatePage;
    @FXML private VBox cancelPage;
    @FXML private VBox completePage;
    @FXML private VBox historyPage;

    @FXML
    public void initialize() {
        bookingRoot.addEventHandler(NavigationEvents.SHOW_BOOKING_MENU, event -> {
            showPage(menuPage);
            event.consume();
        });
        bookingRoot.addEventHandler(NavigationEvents.SHOW_CREATE_BOOKING, event -> {
            showPage(createPage);
            event.consume();
        });
        bookingRoot.addEventHandler(NavigationEvents.SHOW_UPDATE_BOOKING, event -> {
            showPage(updatePage);
            event.consume();
        });
        bookingRoot.addEventHandler(NavigationEvents.SHOW_CANCEL_BOOKING, event -> {
            showPage(cancelPage);
            event.consume();
        });
        bookingRoot.addEventHandler(NavigationEvents.SHOW_COMPLETE_BOOKING, event -> {
            showPage(completePage);
            event.consume();
        });
        bookingRoot.addEventHandler(NavigationEvents.SHOW_BOOKING_HISTORY, event -> {
            showPage(historyPage);
            event.consume();
        });

        showPage(menuPage);
    }

    private void showPage(VBox selectedPage) {
        setPageVisible(menuPage, selectedPage == menuPage);
        setPageVisible(createPage, selectedPage == createPage);
        setPageVisible(updatePage, selectedPage == updatePage);
        setPageVisible(cancelPage, selectedPage == cancelPage);
        setPageVisible(completePage, selectedPage == completePage);
        setPageVisible(historyPage, selectedPage == historyPage);
    }

    private void setPageVisible(VBox page, boolean visible) {
        page.setVisible(visible);
        page.setManaged(visible);
    }
}
