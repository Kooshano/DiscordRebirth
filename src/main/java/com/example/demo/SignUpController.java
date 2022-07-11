package com.example.demo;

import Client.ClientApp;
import Client.SavedData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {
    @FXML
    TextField emailTextField;
    @FXML
    TextField usernameTextField;
    @FXML
    TextField phoneTextField;
    @FXML
    PasswordField passwordField;
    @FXML
    Label messageLabel;


    private Stage stage;
    private Scene scene;
    private Parent root;

    public SignUpController() throws IOException {
    }

    public void createAccount(ActionEvent event) throws IOException, InterruptedException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignInPage.fxml"));
        root = loader.load();

        String email = emailTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        String phone = phoneTextField.getText();
        String state = SavedData.getClientApp().signUp(username,password,email,phone);
        if (state.equals("Sign Up Was Successful")) {
            System.out.println(state);
            messageLabel.setText(state);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setTitle("Discord");
            stage.setScene(scene);
            stage.show();
            messageLabel.setText("");
        }
        else {
            messageLabel.setText(state);
        }
    }

    public void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignInPage.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Discord");
        stage.setScene(scene);
        stage.show();
    }



}
