package com.example.demo;

import Client.*;
import Client.SavedData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;

public class FriendController {

    @FXML
    private TextField addFriendText;
    @FXML
    private Label friendRequestResultLabel;
    @FXML
    private ListView allFriendsListView;
    @FXML
    private ListView pendingRequests;

    public void addFriend(ActionEvent event) throws InterruptedException {
        if (friendRequestResultLabel.equals("Hm, didn't work. Double check that the capitalization,\n" +
                "spelling, any spaces, and numbers are correct.")){
            friendRequestResultLabel.setText("");
            Thread.sleep(500);
        }
        SavedData.getClientApp().sendFriendRequest(addFriendText.getText());
        Thread.sleep(1000);
        System.out.println(SavedData.getFriendRequestResponse());
        String result = SavedData.getFriendRequestResponse();
        if (result.equals("Hm, didn't work. Double check that the capitalization,\n" +
                "spelling, any spaces, and numbers are correct.")){
            friendRequestResultLabel.setTextFill(RED);
        }
        else {
            friendRequestResultLabel.setTextFill(GREEN);
        }
        friendRequestResultLabel.setText(result);
    }

    public void showPendingRequests() throws IOException {
        SavedData.getClientApp().showFriendsRequest();
        System.out.println("damoon" + SavedData.getFriendRequests());
        pendingRequests.getItems().removeAll();
        System.out.println("666" + pendingRequests.getItems());
        pendingRequests.getItems().addAll(SavedData.getFriendRequests());

    }


}
