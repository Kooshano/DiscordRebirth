package com.example.demo;

import Client.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ChangerController {

    @FXML
    Label messageLabel;
    @FXML
    TextField changeTextField;

    private Client client;
    private String changeWhat;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public ChangerController() throws IOException {
    }


    public void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountPage.fxml"));
        root = loader.load();

        AccountPageController accountPageController = loader.getController();
        accountPageController.setClient(client);
        accountPageController.displayColor();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setResizable(false);
        scene = new Scene(root);
        stage.setTitle("Discord");
        stage.setScene(scene);
        stage.show();
    }

    public void setClient(Client client) {
        this.client = client;
    }


    public void change(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountPage.fxml"));
        root = loader.load();


        if (changeWhat.equals("password")){
            SavedData.getClientApp().changePassword(changeTextField.getText());
        }

        AccountPageController accountPageController = loader.getController();
        accountPageController.setClient(client);
        accountPageController.displayColor();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setResizable(false);
        scene = new Scene(root);
        stage.setTitle("Discord");
        stage.setScene(scene);
        stage.show();
    }

    public void setChangeWhat(String changeWhat) {
        this.changeWhat = changeWhat;
    }

    public Label getMessageLabel() {
        return messageLabel;
    }
}
