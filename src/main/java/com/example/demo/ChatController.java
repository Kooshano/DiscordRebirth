package com.example.demo;

import Client.SavedData;
import Model.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ChatController {

    @FXML
    private TextField message;
    @FXML
    private Label chatLabel;
    private String username;

    public void sendMessage(ActionEvent event){
        SavedData.getClientApp().sendPrivateChat(username,message.getText());
        String temp = chatLabel.getText();
        temp +="\n";
        temp += SavedData.getClient().getUsername() + ": " + message.getText();
        message.setText(temp);

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
}
