package com.example.demo;

import Client.Client;
import Client.ClientApp;
import Client.SavedData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class SignInController {
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label messageLabel;

    private Client client;
    
    private Stage stage;
    private Scene scene;
    private Parent root;

    public SignInController() throws IOException {
    }

    @FXML
    public void login(ActionEvent event) throws IOException, InterruptedException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountPage.fxml"));
        root = loader.load();
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        String state = SavedData.getClientApp().signIn(username,password);
        Client client = SavedData.getClientApp().getClient();
        AccountPageController accountPageController = loader.getController();
        accountPageController.setClient(client);
        accountPageController.displayColor();

        if (state.equals("Signed In Successfully")) {
            messageLabel.setText(state);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setResizable(false);
            scene = new Scene(root);
            stage.setTitle("Discord");
            stage.setScene(scene);
            stage.show();
        }
        else {
            messageLabel.setText(state);
        }
    }

    @FXML
    public void goToSignUp(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUpPage.fxml"));
        root =loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Discord");
        stage.setScene(scene);
        stage.show();
    }

    public void setClientApp(Client client) {
        this.client = client;
    }
}