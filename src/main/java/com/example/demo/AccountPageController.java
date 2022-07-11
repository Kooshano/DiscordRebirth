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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.paint.Color.*;

public class AccountPageController {

    @FXML
    Label usernameLabel;
    @FXML
    Label emailLabel;
    @FXML
    Label phoneLabel;
    @FXML
    Pane informationPane;
    @FXML
    Pane wholePain;

    private ClientApp clientApp = new ClientApp();

    private Stage stage;
    private Scene scene;
    private Parent root;

    private static String  username;
    private static String email;
    private static String phone;

    public AccountPageController() throws IOException {
    }

    public void display(String username,String email,String phone){
        AccountPageController.username = username;
        AccountPageController.email = email;
        AccountPageController.phone =phone;

    }

    public void displayColor(){
        usernameLabel.setText(username);
        emailLabel.setText(email);
        phoneLabel.setText(phone);
        informationPane.setBackground(new Background(new BackgroundFill(BLACK,null,null)));
        wholePain.setBackground(new Background(new BackgroundFill(GRAY,null,null)));
    }

    public void logOut(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignInPage.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Discord");
        stage.setScene(scene);
        stage.show();
    }

    public void change(String changeWhat,ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChangerPage.fxml"));
        root = loader.load();

        ChangerController changerController = loader.getController();
        changerController.change(changeWhat);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Discord");
        stage.setScene(scene);
        stage.show();
    }

    public void changePassword(ActionEvent event) throws IOException {
        change("password",event);
    }

    public void changeUsername(ActionEvent event) throws IOException {
        change("username",event);
    }

    public void changeEmail(ActionEvent event) throws IOException {
        change("email",event);
    }
    public void changePhone(ActionEvent event) throws IOException {
        change("phone",event);
    }

}
