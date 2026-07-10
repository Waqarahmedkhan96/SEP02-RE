package view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VehicleManagementController {

    @FXML
    private VBox menuPage;

    @FXML
    private void showAddVehicle() throws IOException {
        showManageVehicle("add");
    }

    @FXML
    private void showUpdateVehicle() throws IOException {
        showManageVehicle("update");
    }

    @FXML
    private void showRemoveVehicle() throws IOException {
        showManageVehicle("remove");
    }

   @FXML
   private void showViewVehicle() throws IOException {
    showVehicleScreen("ViewVehicle.fxml");
  }

  private void showManageVehicle(String mode) throws IOException {
    Stage stage = (Stage) menuPage.getScene().getWindow();

    double width = stage.getWidth();
    double height = stage.getHeight();

    FXMLLoader loader =
    new FXMLLoader(getClass().getResource("ManageVehicle.fxml"));

    Parent root = loader.load();
    ManageVehicleController controller = loader.getController();
    controller.setMode(mode);

    Scene scene = new Scene(root);

    stage.setScene(scene);
    stage.setWidth(width);
    stage.setHeight(height);

    stage.centerOnScreen();
  }

  private void showVehicleScreen(String fxmlFile) throws IOException {
    Stage stage = (Stage) menuPage.getScene().getWindow();

    double width = stage.getWidth();
    double height = stage.getHeight();

    FXMLLoader loader =
    new FXMLLoader(getClass().getResource(fxmlFile));

    Parent root = loader.load();

    Scene scene = new Scene(root);

    stage.setScene(scene);
    stage.setWidth(width);
    stage.setHeight(height);

    stage.centerOnScreen();
  }

  @FXML
  private void handleBackToDashboard() throws IOException {

    Stage stage = (Stage) menuPage.getScene().getWindow();

    double width = stage.getWidth();
    double height = stage.getHeight();

    FXMLLoader loader =
    new FXMLLoader(getClass().getResource("MainView.fxml"));

    Parent root = loader.load();

    stage.setScene(new Scene(root));

    stage.setWidth(width);
    stage.setHeight(height);
    stage.centerOnScreen();
  }
  
}
