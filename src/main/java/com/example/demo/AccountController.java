package com.example.demo;

import Client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AccountController {
    private Client client;

    public void moveToProfile(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfilePage.fxml"));
        Parent root = loader.load();
        ProfilePageController profilePageController = loader.getController();
        profilePageController.setClient(client);
        profilePageController.display();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setResizable(false);
        Scene scene = new Scene(root);
        stage.setTitle("Discord");
        stage.setScene(scene);
        stage.show();
    }

    public void moveToFriend(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Friends.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FriendController friendController = loader.getController();
        friendController.setClient(client);
        stage.setResizable(false);
        Scene scene = new Scene(root);
        stage.setTitle("Discord");
        stage.setScene(scene);
        stage.show();
    }

    public void moveToPrivateChat(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GoToChat.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setResizable(false);
        Scene scene = new Scene(root);
        stage.setTitle("Discord");
        stage.setScene(scene);
        stage.show();
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
