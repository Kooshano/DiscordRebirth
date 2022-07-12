package com.example.demo;

import Client.Client;
import Client.SavedData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
        Thread.sleep(500);
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

    public void showPendingRequests(){
        System.out.println("ssss");
        SavedData.getClientApp().showFriendsRequest();
        
        System.out.println(SavedData.getFriendRequests());
        pendingRequests.getItems().addAll(SavedData.getFriendRequests());
    }


}
