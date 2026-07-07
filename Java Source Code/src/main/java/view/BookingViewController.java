package view;

import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mockdata.MockBookings;
import mockdata.MockCustomers;
import mockdata.MockVehicles;
import model.Booking;
import model.Customer;
import model.Vehicle;
import viewmodel.BookingViewModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

public class BookingViewController {

    @FXML private VBox menuPage;
    @FXML private VBox createPage;
    @FXML private VBox updatePage;
    @FXML private VBox cancelPage;
    @FXML private VBox completePage;
    @FXML private VBox historyPage;

    @FXML private TextField customerIdField;
    @FXML private ComboBox<String> customerComboBox;
    @FXML private TextField bookingIdField;
    @FXML private TextField vehicleIdField;
    @FXML private TextField updateVehicleIdField;
    @FXML private TextField employeeIdField;
    @FXML private ComboBox<String> employeeIdComboBox;
    @FXML private TextField colorFilterField;
    @FXML private TextField vehicleTypeFilterField;
    @FXML private TextField maxPriceFilterField;
    @FXML private TextField vehicleStatusFilterField;
    @FXML private TextField cancelSearchField;
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
    @FXML private Label selectedVehicleStatusLabel;
    @FXML private Button confirmBookingButton;
    @FXML private Label cancelSelectionLabel;
    @FXML private Button confirmCancelBookingButton;

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
    @FXML private TextField historyCustomerFilterField;
    @FXML private TextField historyVehicleFilterField;
    @FXML private TextField historyDateFilterField;
    @FXML private Label historyDetailsLabel;

    @FXML private Label statusLabel;

    private final BookingViewModel viewModel = new BookingViewModel();
    private final ObservableList<Vehicle> mockVehicles = FXCollections.observableArrayList(MockVehicles.getVehicles());
    private final ObservableList<Booking> mockBookings = FXCollections.observableArrayList(MockBookings.getBookings());

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

        setupCreateBookingSelectors();
        bookingsTable.setItems(viewModel.customerBookings);
        createVehiclesTable.setItems(mockVehicles);
        createVehiclesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVehicle, selectedVehicle) -> {
            if (selectedVehicle != null) {
                vehicleIdField.setText(String.valueOf(selectedVehicle.getVehicleId()));
                updateSelectedVehicleStatus(selectedVehicle);
            }
        });
        cancelBookingsTable.setItems(viewModel.cancellableBookings);
        completeBookingsTable.setItems(mockBookings);
        historyBookingsTable.setItems(viewModel.archivedBookings);
        bookingsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldBooking, selectedBooking) ->
                fillUpdateForm(selectedBooking));
        cancelBookingsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldBooking, selectedBooking) ->
                updateCancelSelection(selectedBooking));
        historyBookingsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldBooking, selectedBooking) ->
                showArchivedBookingDetails(selectedBooking));

        statusLabel.textProperty().bind(viewModel.statusMessage);
        statusLabel.visibleProperty().bind(viewModel.statusMessage.isNotEmpty());
        statusLabel.managedProperty().bind(statusLabel.visibleProperty());
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
        handleShowAvailableVehicles();
    }

    @FXML
    private void showUpdateBooking() {
        showPage(updatePage);
        showMockActiveBookings();
    }

    @FXML
    private void showCancelBooking() {
        showPage(cancelPage);
        handleSearchCancellableBookings();
    }

    @FXML
    private void showCompleteBooking() {
        showPage(completePage);
    }

    @FXML
    private void showBookingHistory() {
        showPage(historyPage);
        handleApplyHistoryFilters();
    }

    @FXML
    private void handleApplyHistoryFilters() {
        viewModel.loadArchivedBookings(
                historyCustomerFilterField.getText(),
                historyVehicleFilterField.getText(),
                historyDateFilterField.getText()
        );
        historyBookingsTable.getSelectionModel().clearSelection();
        historyDetailsLabel.setText("Select an archived booking to view details.");
    }

    @FXML
    private void handleClearHistoryFilters() {
        historyCustomerFilterField.clear();
        historyVehicleFilterField.clear();
        historyDateFilterField.clear();
        handleApplyHistoryFilters();
    }

    @FXML
    private void handleBackToDashboard() {
        menuPage.getParent().fireEvent(new Event(NavigationEvents.SHOW_DASHBOARD));
    }

    @FXML
    private void handleCreateBooking() {
        if (!isCreateFormValid()) {
            return;
        }
        viewModel.submit();
        viewModel.loadCustomerBookings();
    }

    @FXML
    private void handleShowAvailableVehicles() {
        String color = normalize(colorFilterField.getText());
        String type = normalize(vehicleTypeFilterField.getText());
        double maxPrice = parseDoubleOrZero(maxPriceFilterField.getText());

        ObservableList<Vehicle> filteredVehicles = MockVehicles.getVehicles().stream()
                .filter(vehicle -> "available".equalsIgnoreCase(vehicle.getCurrentState()))
                .filter(vehicle -> color.isBlank() || contains(vehicle.getColor(), color))
                .filter(vehicle -> type.isBlank() || contains(vehicle.getVehicleType(), type))
                .filter(vehicle -> maxPrice <= 0 || vehicle.getPriceHour() <= maxPrice)
                .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);

        createVehiclesTable.setItems(filteredVehicles);
        createVehiclesTable.getSelectionModel().clearSelection();
        vehicleIdField.clear();
        resetSelectedVehicleStatus();
        viewModel.statusMessage.set("Showing " + filteredVehicles.size() + " available mocked vehicle(s)");
    }

    @FXML
    private void handleSearchActiveBookings() {
        viewModel.loadActiveCustomerBookings();
        if (viewModel.customerBookings.isEmpty()) {
            showMockActiveBookings();
        }
    }

    @FXML
    private void handleUpdateBooking() {
        copyUpdateFieldsToBoundCreateFields();
        if (viewModel.updateBooking()) {
            viewModel.loadActiveCustomerBookings();
        }
    }

    @FXML
    private void handleSearchCancellableBookings() {
        String query = cancelSearchField == null ? "" : cancelSearchField.getText();
        boolean loadedFromServer = viewModel.searchCancellableBookings(query);
        if (!loadedFromServer) {
            showMockCancellableBookings(query);
        }
        cancelBookingsTable.getSelectionModel().clearSelection();
        updateCancelSelection(null);
    }

    @FXML
    private void handleCancelBooking() {
        Booking selectedBooking = cancelBookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            viewModel.statusMessage.set("Select a booking to cancel");
            return;
        }

        if (!selectedBooking.getStartDate().isAfter(LocalDateTime.now())) {
            viewModel.statusMessage.set("The booking period has already started, so the booking cannot be cancelled");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Cancel Booking");
        confirmation.setHeaderText("Cancel booking #" + selectedBooking.getBookingId() + "?");
        confirmation.setContentText("The booking will be marked as cancelled and the vehicle will be released for this period.");
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            viewModel.statusMessage.set("Cancellation process cancelled");
            return;
        }

        if (viewModel.cancelBooking(selectedBooking.getBookingId())) {
            handleSearchCancellableBookings();
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

    private void setupCreateBookingSelectors() {
        ObservableList<String> customers = MockCustomers.getCustomers().stream()
                .map(this::formatCustomerOption)
                .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);
        customerComboBox.setItems(customers);
        customerComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selectedValue) ->
                customerIdField.setText(String.valueOf(parseLeadingIntOrZero(selectedValue))));

        employeeIdComboBox.setItems(FXCollections.observableArrayList("999"));
        employeeIdComboBox.getSelectionModel().select("999");
        employeeIdField.setText("999");
        employeeIdComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selectedValue) ->
                employeeIdField.setText(selectedValue == null ? "" : selectedValue));
    }

    private void updateSelectedVehicleStatus(Vehicle selectedVehicle) {
        boolean available = "available".equalsIgnoreCase(selectedVehicle.getCurrentState());
        selectedVehicleStatusLabel.setText(available ? "Available" : selectedVehicle.getCurrentState());
        selectedVehicleStatusLabel.setStyle(available
                ? "-fx-background-color: #d9f3e3; -fx-text-fill: #159447; -fx-background-radius: 16; -fx-padding: 7 20 7 20; -fx-font-weight: bold;"
                : "-fx-background-color: #f8d7da; -fx-text-fill: #b00020; -fx-background-radius: 16; -fx-padding: 7 20 7 20; -fx-font-weight: bold;");
        confirmBookingButton.setDisable(!available);
        if (!available) {
            viewModel.statusMessage.set("Selected vehicle is " + selectedVehicle.getCurrentState() + " and cannot be booked");
        }
    }

    private void resetSelectedVehicleStatus() {
        selectedVehicleStatusLabel.setText("Select vehicle");
        selectedVehicleStatusLabel.setStyle("-fx-background-color: #eeeeee; -fx-text-fill: #777777; -fx-background-radius: 16; -fx-padding: 7 20 7 20; -fx-font-weight: bold;");
        confirmBookingButton.setDisable(false);
    }

    private boolean isCreateFormValid() {
        Vehicle selectedVehicle = createVehiclesTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            viewModel.statusMessage.set("Select an available vehicle first");
            return false;
        }
        if (!"available".equalsIgnoreCase(selectedVehicle.getCurrentState())) {
            viewModel.statusMessage.set("Selected vehicle is " + selectedVehicle.getCurrentState() + " and cannot be booked");
            return false;
        }
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        if (startDate == null || endDate == null) {
            viewModel.statusMessage.set("Select start and end dates");
            return false;
        }
        if (!startDate.isBefore(endDate)) {
            viewModel.statusMessage.set("Start date must be before end date");
            return false;
        }
        if (customerIdField.getText().isBlank() || employeeIdField.getText().isBlank()) {
            viewModel.statusMessage.set("Select a customer and employee");
            return false;
        }
        return true;
    }

    private void showMockActiveBookings() {
        ObservableList<Booking> activeBookings = MockBookings.getBookings().stream()
                .filter(booking -> "ACTIVE".equalsIgnoreCase(booking.getBookingStatus()))
                .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);
        viewModel.customerBookings.setAll(activeBookings);
        viewModel.statusMessage.set("Showing " + activeBookings.size() + " mocked active booking(s)");
    }

    private void showMockCancellableBookings(String query) {
        String normalizedQuery = normalize(query);
        ObservableList<Booking> cancellableBookings = MockBookings.getBookings().stream()
                .filter(booking -> "ACTIVE".equalsIgnoreCase(booking.getBookingStatus())
                        || "PENDING".equalsIgnoreCase(booking.getBookingStatus()))
                .filter(booking -> normalizedQuery.isBlank()
                        || String.valueOf(booking.getBookingId()).contains(normalizedQuery)
                        || String.valueOf(booking.getCustomerId()).contains(normalizedQuery)
                        || String.valueOf(booking.getVehicleId()).contains(normalizedQuery))
                .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);
        viewModel.cancellableBookings.setAll(cancellableBookings);
        viewModel.statusMessage.set(cancellableBookings.isEmpty()
                ? "Booking cannot be found"
                : "Showing " + cancellableBookings.size() + " mocked cancellable booking(s)");
    }

    private void updateCancelSelection(Booking selectedBooking) {
        boolean hasSelection = selectedBooking != null;
        confirmCancelBookingButton.setDisable(!hasSelection);
        if (!hasSelection) {
            cancelSelectionLabel.setText("Select a booking to cancel");
            return;
        }

        cancelSelectionLabel.setText("Booking #" + selectedBooking.getBookingId()
                + " | Vehicle #" + selectedBooking.getVehicleId()
                + " | " + selectedBooking.getStartDate().toLocalDate()
                + " to " + selectedBooking.getEndDate().toLocalDate());
    }

    private void showArchivedBookingDetails(Booking booking) {
        if (booking == null) {
            return;
        }

        String actualReturn = booking.getActualReturnDate() == null
                ? "Not recorded"
                : String.valueOf(booking.getActualReturnDate());
        historyDetailsLabel.setText("Booking #" + booking.getBookingId()
                + " | Customer ID: " + booking.getCustomerId()
                + " | Vehicle ID: " + booking.getVehicleId()
                + " | Employee ID: " + booking.getEmployeeId()
                + " | Start: " + booking.getStartDate()
                + " | End: " + booking.getEndDate()
                + " | Returned: " + actualReturn
                + " | Status: " + booking.getBookingStatus());
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

    private double parseDoubleOrZero(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private boolean contains(String value, String filter) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(filter);
    }

    private String formatCustomerOption(Customer customer) {
        return customer.getCustomerId() + " - " + customer.getName();
    }

    private int parseLeadingIntOrZero(String value) {
        if (value == null) {
            return 0;
        }
        String id = value.split("-", 2)[0].trim();
        return parseIntOrZero(id);
    }
}
