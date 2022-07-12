package com.example.demo;

import Client.SavedData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FriendController {

    @FXML
    private TextField addFriendText;
    @FXML
    private Button friendRequestButton;
    @FXML
    private Label friendRequestResultLabel;
    public void addFriend(ActionEvent event){
        SavedData.getClientApp().sendFriendRequest(addFriendText.getText());
        friendRequestButton.setText(SavedData.getFriendRequestResponse);
    }

}
