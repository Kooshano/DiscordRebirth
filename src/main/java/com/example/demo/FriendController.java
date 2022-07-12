package com.example.demo;

import Client.*;
import Client.SavedData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;

public class FriendController implements Initializable {

    @FXML
    private TextField addFriendText;
    @FXML
    private Label friendRequestResultLabel;
    @FXML
    private ListView allFriendsListView;
    @FXML
    private ListView pendingRequests;
    @FXML
    private Label chosenPending;
    @FXML
    private ListView onlineFriendsListView;
    @FXML
    private TextField blockText;

    private Client client;

    private Parent root;
    private Scene scene;
    private Stage stage;

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

    public void showPendingRequests() throws IOException, InterruptedException {
        SavedData.getClientApp().showFriendsRequest();
        Thread.sleep(500);
        pendingRequests.getItems().setAll(SavedData.getFriendRequests());


    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pendingRequests.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                chosenPending.setText((String)pendingRequests.getSelectionModel().getSelectedItem());
            }
        });
    }

    public void acceptPending(ActionEvent event){
        System.out.println(chosenPending.getText());
        SavedData.getClientApp().answerFriendRequest(chosenPending.getText(),"yes");
        Object delete = null;
        for (Object i :pendingRequests.getItems()){
            String name = (String) i;
            if (name.equals(chosenPending.getText())) {
                delete = i;
                break;
            }
        }
        pendingRequests.getItems().remove(delete);
    }

    public void rejectPending(ActionEvent event){
        SavedData.getClientApp().answerFriendRequest(chosenPending.getText(),"no");

        Object delete = null;
        for (Object i :pendingRequests.getItems()){
            String name = (String) i;
            if (name.equals(chosenPending.getText())) {
                delete = i;
                break;
            }
        }
        pendingRequests.getItems().remove(delete);
    }

    public void showAllFriends() throws InterruptedException {
        SavedData.getClientApp().showFriends();
        Thread.sleep(500);
        allFriendsListView.getItems().setAll(SavedData.getFriends());
    }

    public void showOnlineFriends() throws InterruptedException {
        ArrayList empty = new ArrayList<>();
        onlineFriendsListView.getItems().setAll(empty);
        SavedData.getClientApp().showFriends();
        Thread.sleep(500);
        ArrayList<String> friends = SavedData.getFriends();
        for (String s : friends){
            if (s.split(": ")[1].equals("Online")){
                onlineFriendsListView.getItems().addAll(s.split(": ")[0]);
            }
        }

    }

    public void block(){
        SavedData.getClientApp().block(blockText.getText());
    }

    public void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Account.fxml"));
        root = loader.load();
        AccountController accountController = loader.getController();
        accountController.setClient(client);
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




}
