package com.example.demo;

import Client.SavedData;
import Model.Message;
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

public class ChatController {

    @FXML
    private TextField message;
    @FXML
    private Label chatLabel;
    private String username;

    public void sendMessage(ActionEvent event) throws InterruptedException {
        if (!message.getText().equals("")) {
            SavedData.getClientApp().sendPrivateChat(username, message.getText());
            String temp = chatLabel.getText();
            temp += "\n";
            temp += SavedData.getClientApp().getClient().getUsername() + ": " + message.getText();
            chatLabel.setText(temp);
            message.setText("");
            SavedData.getClientApp().openPrivateChat(username);
            Thread.sleep(500);
            chatHistory();
        }
        else {
            chatHistory();
        }

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void chatHistory(){
        StringBuilder stringBuilder = new StringBuilder();
        for (Message message : SavedData.getCurrentChatMessages()){
            stringBuilder.append(message.getSender() + ": " + message.getBody() + "\n");
        }
        chatLabel.setText(String.valueOf(stringBuilder));
    }

    public void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Account.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setResizable(false);
        Scene scene = new Scene(root);
        stage.setTitle("Discord");
        stage.setScene(scene);
        stage.show();
    }
}
