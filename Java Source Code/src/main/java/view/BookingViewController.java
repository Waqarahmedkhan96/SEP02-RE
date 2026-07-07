package view;

import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.collections.FXCollections;
import mockdata.MockBookings;
import mockdata.MockVehicles;
import model.Booking;
import model.Vehicle;
import viewmodel.BookingViewModel;

public class BookingViewController {

    @FXML private VBox menuPage;
    @FXML private VBox createPage;
    @FXML private VBox updatePage;
    @FXML private VBox cancelPage;
    @FXML private VBox completePage;
    @FXML private VBox historyPage;

    @FXML private TextField customerIdField;
    @FXML private TextField bookingIdField;
    @FXML private TextField vehicleIdField;
    @FXML private TextField updateVehicleIdField;
    @FXML private TextField employeeIdField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private DatePicker updateStartDatePicker;
    @FXML private DatePicker updateEndDatePicker;
    @FXML private TextField statusField;

    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, Number> bookingIdColumn;
    @FXML private TableColumn<Booking, String> startDateColumn;
    @FXML private TableColumn<Booking, String> endDateColumn;
    @FXML private TableColumn<Booking, String> statusColumn;
    @FXML private TableColumn<Booking, Number> vehicleIdColumn;

    @FXML private TableView<Vehicle> createVehiclesTable;
    @FXML private TableColumn<Vehicle, Number> createVehicleIdColumn;
    @FXML private TableColumn<Vehicle, String> createVehicleModelColumn;
    @FXML private TableColumn<Vehicle, String> createVehicleTypeColumn;
    @FXML private TableColumn<Vehicle, String> createVehicleColorColumn;
    @FXML private TableColumn<Vehicle, Number> createVehiclePriceColumn;
    @FXML private TableColumn<Vehicle, String> createVehicleStatusColumn;

    @FXML private TableView<Booking> cancelBookingsTable;
    @FXML private TableColumn<Booking, Number> cancelBookingIdColumn;
    @FXML private TableColumn<Booking, String> cancelStartDateColumn;
    @FXML private TableColumn<Booking, String> cancelEndDateColumn;
    @FXML private TableColumn<Booking, String> cancelStatusColumn;
    @FXML private TableColumn<Booking, Number> cancelVehicleIdColumn;

    @FXML private TableView<Booking> completeBookingsTable;
    @FXML private TableColumn<Booking, Number> completeBookingIdColumn;
    @FXML private TableColumn<Booking, String> completeStartDateColumn;
    @FXML private TableColumn<Booking, String> completeEndDateColumn;
    @FXML private TableColumn<Booking, String> completeStatusColumn;
    @FXML private TableColumn<Booking, Number> completeVehicleIdColumn;

    @FXML private TableView<Booking> historyBookingsTable;
    @FXML private TableColumn<Booking, Number> historyBookingIdColumn;
    @FXML private TableColumn<Booking, String> historyStartDateColumn;
    @FXML private TableColumn<Booking, String> historyEndDateColumn;
    @FXML private TableColumn<Booking, String> historyStatusColumn;
    @FXML private TableColumn<Booking, Number> historyVehicleIdColumn;

    @FXML private Label statusLabel;

    private final BookingViewModel viewModel = new BookingViewModel();

    @FXML
    public void initialize() {
        viewModel.customerId.bind(customerIdField.textProperty().map(this::parseIntOrZero));
        viewModel.bookingId.bind(bookingIdField.textProperty().map(this::parseIntOrZero));
        viewModel.vehicleId.bind(vehicleIdField.textProperty().map(this::parseIntOrZero));
        viewModel.employeeId.bind(employeeIdField.textProperty().map(this::parseIntOrZero));
        viewModel.startDate.bind(startDatePicker.valueProperty().map(String::valueOf));
        viewModel.endDate.bind(endDatePicker.valueProperty().map(String::valueOf));
        viewModel.bookingStatus.bind(statusField.textProperty());

        wireBookingTable(bookingsTable, bookingIdColumn, startDateColumn, endDateColumn, statusColumn, vehicleIdColumn);
        wireVehicleTable();
        wireBookingTable(cancelBookingsTable, cancelBookingIdColumn, cancelStartDateColumn, cancelEndDateColumn,
                cancelStatusColumn, cancelVehicleIdColumn);
        wireBookingTable(completeBookingsTable, completeBookingIdColumn, completeStartDateColumn, completeEndDateColumn,
                completeStatusColumn, completeVehicleIdColumn);
        wireBookingTable(historyBookingsTable, historyBookingIdColumn, historyStartDateColumn, historyEndDateColumn,
                historyStatusColumn, historyVehicleIdColumn);

        bookingsTable.setItems(viewModel.customerBookings);
        createVehiclesTable.setItems(FXCollections.observableArrayList(MockVehicles.getVehicles()));
        createVehiclesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVehicle, selectedVehicle) -> {
            if (selectedVehicle != null) {
                vehicleIdField.setText(String.valueOf(selectedVehicle.getVehicleId()));
            }
        });
        cancelBookingsTable.setItems(FXCollections.observableArrayList(MockBookings.getBookings()));
        completeBookingsTable.setItems(FXCollections.observableArrayList(MockBookings.getBookings()));
        historyBookingsTable.setItems(FXCollections.observableArrayList(MockBookings.getBookings()));
        bookingsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldBooking, selectedBooking) ->
                fillUpdateForm(selectedBooking));

        statusLabel.textProperty().bind(viewModel.statusMessage);
        addButtonAnimations(menuPage, createPage, updatePage, cancelPage, completePage, historyPage);
        showPage(menuPage);
    }

    @FXML
    private void showBookingMenu() {
        showPage(menuPage);
    }

    @FXML
    private void showCreateBooking() {
        showPage(createPage);
    }

    @FXML
    private void showUpdateBooking() {
        showPage(updatePage);
    }

    @FXML
    private void showCancelBooking() {
        showPage(cancelPage);
    }

    @FXML
    private void showCompleteBooking() {
        showPage(completePage);
    }

    @FXML
    private void showBookingHistory() {
        showPage(historyPage);
    }

    @FXML
    private void handleBackToDashboard() {
        menuPage.fireEvent(new Event(NavigationEvents.SHOW_DASHBOARD));
    }

    @FXML
    private void handleCreateBooking() {
        viewModel.submit();
        viewModel.loadCustomerBookings();
    }

    @FXML
    private void handleLoadBookings() {
        viewModel.loadCustomerBookings();
    }

    @FXML
    private void handleSearchActiveBookings() {
        viewModel.loadActiveCustomerBookings();
    }

    @FXML
    private void handleUpdateBooking() {
        copyUpdateFieldsToBoundCreateFields();
        if (viewModel.updateBooking()) {
            viewModel.loadActiveCustomerBookings();
        }
    }

    private void wireBookingTable(TableView<Booking> table,
                                  TableColumn<Booking, Number> bookingColumn,
                                  TableColumn<Booking, String> startColumn,
                                  TableColumn<Booking, String> endColumn,
                                  TableColumn<Booking, String> bookingStatusColumn,
                                  TableColumn<Booking, Number> vehicleColumn) {
        bookingColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getBookingId()));
        startColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getStartDate())));
        endColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getEndDate())));
        bookingStatusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookingStatus()));
        vehicleColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getVehicleId()));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void wireVehicleTable() {
        createVehicleIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getVehicleId()));
        createVehicleModelColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getModel()));
        createVehicleTypeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVehicleType()));
        createVehicleColorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getColor()));
        createVehiclePriceColumn.setCellValueFactory(data -> new SimpleIntegerProperty((int) data.getValue().getPriceHour()));
        createVehicleStatusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCurrentState()));
        createVehiclesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void fillUpdateForm(Booking selectedBooking) {
        if (selectedBooking == null) {
            return;
        }

        bookingIdField.setText(String.valueOf(selectedBooking.getBookingId()));
        customerIdField.setText(String.valueOf(selectedBooking.getCustomerId()));
        vehicleIdField.setText(String.valueOf(selectedBooking.getVehicleId()));
        updateVehicleIdField.setText(String.valueOf(selectedBooking.getVehicleId()));
        employeeIdField.setText(String.valueOf(selectedBooking.getEmployeeId()));
        startDatePicker.setValue(selectedBooking.getStartDate().toLocalDate());
        endDatePicker.setValue(selectedBooking.getEndDate().toLocalDate());
        updateStartDatePicker.setValue(selectedBooking.getStartDate().toLocalDate());
        updateEndDatePicker.setValue(selectedBooking.getEndDate().toLocalDate());
        statusField.setText(selectedBooking.getBookingStatus());
    }

    private void copyUpdateFieldsToBoundCreateFields() {
        vehicleIdField.setText(updateVehicleIdField.getText());
        startDatePicker.setValue(updateStartDatePicker.getValue());
        endDatePicker.setValue(updateEndDatePicker.getValue());
        statusField.setText("ACTIVE");
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

    private void addButtonAnimations(Node... roots) {
        for (Node root : roots) {
            root.lookupAll(".button").forEach(this::addButtonAnimation);
        }
    }

    private void addButtonAnimation(Node button) {
        button.setOnMouseEntered(event -> animateButton(button, 1.04));
        button.setOnMouseExited(event -> animateButton(button, 1.0));
        button.setOnMousePressed(event -> animateButton(button, 0.97));
        button.setOnMouseReleased(event -> animateButton(button, 1.04));
    }

    private void animateButton(Node button, double scale) {
        ScaleTransition transition = new ScaleTransition(Duration.millis(110), button);
        transition.setToX(scale);
        transition.setToY(scale);
        transition.play();
    }

    private int parseIntOrZero(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
