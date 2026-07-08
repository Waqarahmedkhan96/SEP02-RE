package view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import viewmodel.CustomerViewModel;

public class CustomerViewController {

    @FXML private TextField nameField, phoneField, emailField, cprField, passportField, licenseField;

    private final CustomerViewModel viewModel = new CustomerViewModel();

    @FXML
    public void initialize() {
        viewModel.name.bind(nameField.textProperty());
        viewModel.phoneNo.bind(phoneField.textProperty());
        viewModel.email.bind(emailField.textProperty());
        viewModel.cpr.bind(cprField.textProperty());
        viewModel.passportNo.bind(passportField.textProperty());
        viewModel.licenseNo.bind(licenseField.textProperty());
    }

    @FXML
    private void handleSubmit() {
        viewModel.submit();
    }

    @FXML
    private void handleClear() {
        nameField.clear();
        phoneField.clear();
        emailField.clear();
        cprField.clear();
        passportField.clear();
        licenseField.clear();
    }

    @FXML
    private void handleBack() {
        // TODO: navigate back, e.g. close window or switch view
    }
}