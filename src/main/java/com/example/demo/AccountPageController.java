package com.example.demo;

import Client.*;
import Client.ClientApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
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
    Pane wholePane;
    @FXML
    private Pane statePane;

    private Client client ;

    private Stage stage;
    private Scene scene;
    private Parent root;


    public AccountPageController() throws IOException {
    }


    public void displayColor(){
        usernameLabel.setText(client.getUsername());
        emailLabel.setText(client.getEmail());
        phoneLabel.setText(client.getPhone());
        informationPane.setBackground(new Background(new BackgroundFill(BLACK,null,null)));
        wholePane.setBackground(new Background(new BackgroundFill(GRAY,null,null)));
        statePane.setBackground(new Background(new BackgroundFill(SILVER,null,null)));
    }

    public void logOut(ActionEvent event) throws IOException, InterruptedException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignInPage.fxml"));
        root = loader.load();
        SavedData.getClientApp().logOut();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Discord");
        stage.setScene(scene);
        stage.show();
        ClientApp clientApp = new ClientApp();
        SavedData.setClientApp(clientApp);
    }

    public void change(String changeWhat,ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChangerPage.fxml"));
        root = loader.load();

        ChangerController changerController = loader.getController();
        changerController.setClient(client);
        changerController.getMessageLabel().setText("Enter your new " + changeWhat + "\n then press change button");
        changerController.setChangeWhat(changeWhat);

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

    public void setClient(Client client) {
        this.client = client;
    }

    public void stateChange(ActionEvent event){
        RadioButton radioButton = (RadioButton) event.getSource();
        if (radioButton.getText().equals("Online")){
        }
    }
}
