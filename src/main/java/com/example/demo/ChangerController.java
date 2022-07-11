package com.example.demo;

import Client.ClientApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ChangerController {

    @FXML
    Label messageLabel;

    private ClientApp clientApp = new ClientApp();

    private Stage stage;
    private Scene scene;
    private Parent root;

    public ChangerController() throws IOException {
    }


    public void change(String changeWhat){
        messageLabel.setText("Enter your new " + changeWhat + "\n then press change button");

    }

    public void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountPage.fxml"));
        root = loader.load();

        AccountPageController accountPageController = loader.getController();
        accountPageController.displayColor();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setResizable(false);
        scene = new Scene(root);
        stage.setTitle("Discord");
        stage.setScene(scene);
        stage.show();
    }
}
