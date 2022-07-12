package com.example.demo;

import Client.Client;
import Client.SavedData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;

public class FriendController {

    @FXML
    private TextField addFriendText;
    @FXML
    private Label friendRequestResultLabel;
    @FXML
    private ListView allFriendListView;

    public void addFriend(ActionEvent event) throws InterruptedException {
        SavedData.getClientApp().sendFriendRequest(addFriendText.getText());
        Thread.sleep(500);
        System.out.println(SavedData.getFriendRequestResponse());
        String result = SavedData.getFriendRequestResponse();
        if (result.equals()){
            friendRequestResultLabel.setTextFill(RED);
        }
        else {
            friendRequestResultLabel.setTextFill(GREEN);
        }
        friendRequestResultLabel.setText(result);
        

    }


}
