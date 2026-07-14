package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.Customer;
import viewmodel.CustomerViewModel;

public class UpdateCustomerController {

    @FXML private VBox updateRoot;
    @FXML private ComboBox<String> customerComboBox;
    @FXML private TextField nameField, phoneField, emailField, cprField, passportField, licenseField;
    @FXML private CheckBox catA, catB, catC, catD;
    @FXML private Label statusLabel;

    private final CustomerViewModel viewModel = new CustomerViewModel();

    @FXML
    public void initialize() {
        viewModel.name.bind(nameField.textProperty());
        viewModel.phoneNo.bind(phoneField.textProperty());
        viewModel.email.bind(emailField.textProperty());
        viewModel.cpr.bind(cprField.textProperty());
        viewModel.passportNo.bind(passportField.textProperty());
        viewModel.licenseNo.bind(licenseField.textProperty());
        viewModel.categoryA.bind(catA.selectedProperty());
        viewModel.categoryB.bind(catB.selectedProperty());
        viewModel.categoryC.bind(catC.selectedProperty());
        viewModel.categoryD.bind(catD.selectedProperty());

        statusLabel.textProperty().bind(viewModel.statusMessage);
        customerComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selectedValue) ->
                fillFormFromSelectedCustomer(selectedValue));
        viewModel.customers.addListener((javafx.collections.ListChangeListener<Customer>) change ->
                refreshCustomerOptions());

        refreshCustomers();
    }

    @FXML
    private void showCustomerMenu() {
        updateRoot.fireEvent(new javafx.event.Event(NavigationEvents.SHOW_CUSTOMER_MENU));
    }

    @FXML
    private void handleUpdateCustomer() {
        int customerId = parseCustomerId(customerComboBox.getValue());
        if (customerId <= 0) {
            viewModel.statusMessage.set("Select a customer to update");
            return;
        }

        viewModel.updateCustomer(customerId);
    }

    void refreshCustomers() {
        viewModel.loadCustomers();
        refreshCustomerOptions();
    }

    private void refreshCustomerOptions() {
        ObservableList<String> options = viewModel.customers.stream()
                .map(customer -> customer.getCustomerId() + " - " + customer.getName())
                .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);
        customerComboBox.setItems(options);
    }

    private void fillFormFromSelectedCustomer(String selectedValue) {
        int customerId = parseCustomerId(selectedValue);
        if (customerId <= 0) {
            return;
        }

        viewModel.customers.stream()
                .filter(customer -> customer.getCustomerId() == customerId)
                .findFirst()
                .ifPresent(customer -> {
                    nameField.setText(nullToEmpty(customer.getName()));
                    phoneField.setText(nullToEmpty(customer.getPhoneNo()));
                    emailField.setText(nullToEmpty(customer.getEmail()));
                    cprField.setText(nullToEmpty(customer.getCpr()));
                    passportField.setText(nullToEmpty(customer.getPassportNo()));
                    licenseField.setText(nullToEmpty(customer.getLicenseNo()));
                    catA.setSelected(customer.isCategoryA());
                    catB.setSelected(customer.isCategoryB());
                    catC.setSelected(customer.isCategoryC());
                    catD.setSelected(customer.isCategoryD());
                });
    }

    private int parseCustomerId(String selectedValue) {
        if (selectedValue == null || selectedValue.isBlank()) {
            return 0;
        }

        try {
            return Integer.parseInt(selectedValue.split(" - ")[0].trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
