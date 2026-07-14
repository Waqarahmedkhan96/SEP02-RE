package view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import viewmodel.CustomerViewModel;

public class CustomerViewController {

    @FXML private VBox customerRoot;
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
    }

    @FXML
    private void handleSubmit() {
        if (viewModel.submit()) {
            clearForm();
        }
    }

    @FXML
    private void showCustomerMenu() {
        customerRoot.fireEvent(new javafx.event.Event(NavigationEvents.SHOW_CUSTOMER_MENU));
    }

    private void clearForm() {
        nameField.clear();
        phoneField.clear();
        emailField.clear();
        cprField.clear();
        passportField.clear();
        licenseField.clear();
        catA.setSelected(false);
        catB.setSelected(false);
        catC.setSelected(false);
        catD.setSelected(false);
    }
}
