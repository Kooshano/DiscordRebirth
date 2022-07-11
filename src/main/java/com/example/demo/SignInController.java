package com.example.demo;

import Client.ClientApp;
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

    private ClientApp clientApp = new ClientApp();

    private Stage stage;
    private Scene scene;
    private Parent root;

    public SignInController() throws IOException {
        //loginButton.setBackground(new Background(new BackgroundFill(RED,null,null)));
    }

    @FXML
    public void login(ActionEvent event) throws IOException, InterruptedException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountPage.fxml"));
        root = loader.load();
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        String state = clientApp.signIn(username,password);

        AccountPageController accountPageController = loader.getController();
        accountPageController.display(username,"DASD","8569");
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

}