package sample.controllers;

import sample.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Main;

import java.io.IOException;
import java.util.Optional;

public class MainController {

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Button mainSettingsButton;

    @FXML
    private Label mainSettingLabel;

    @FXML
    private Button mainLogoutButton;

    private User user;

    @FXML
    void initialize() {
        // ============= Setting button clicked ==============

        mainSettingsButton.setOnAction(event -> {
            System.out.println("Showing the Setting window and blocking the main window");
            try {
                showSetting();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        mainLogoutButton.setOnAction(event -> {
            try {
                showLogoutAlert();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    // ============== helping functions ===============

    public void hideSetting(){
        mainSettingsButton.setDisable(true);
        mainSettingLabel.setDisable(true);
    }

    public void showSetting() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/sample/view/settings.fxml"));
        fxmlLoader.load();

        SettingsController settingsController = fxmlLoader.getController();
        settingsController.setUser(user);

        Parent root = fxmlLoader.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Settings");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(mainPane.getScene().getWindow());
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait();
    }

    public void logout() throws Exception {
        setUser(null);
        Main main  = new Main();
        mainPane.getScene().getWindow().hide();
        System.out.println("User logged out.");
        main.start(new Stage());
    }

    // ============= Alerts ===========================

    public void showLogoutAlert() throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to log out?");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent()){
            if(result.get() == ButtonType.OK){
                logout();
            }
        }
    }

    // ============= Setter ===========================
    public void setUser(User user) {
        this.user = user;
    }
}
