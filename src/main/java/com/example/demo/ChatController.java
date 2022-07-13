package com.example.demo;

import Client.SavedData;
import Model.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatController {

    @FXML
    private TextField message;
    @FXML
    private Label chatLabel;
    @FXML
    private Pane backGroundPane;
    @FXML
    private ColorPicker backGroundColorPicker;
    @FXML
    private ColorPicker textColorPicker;

    private String username;
    private Color backGroundColor;

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

    public void setBackGroundColor(Color color){
        this.backGroundColor = color;
        backGroundColorPicker.setValue(color);
        backGroundPane.setBackground(new Background(new BackgroundFill(color,null,null)));
        chatLabel.setBackground(new Background(new BackgroundFill(color,null,null)));

    }

    public void changeBackGroundColor(ActionEvent event){
        Color color = backGroundColorPicker.getValue();
        backGroundPane.setBackground(new Background(new BackgroundFill(color,null,null)));
        chatLabel.setBackground(new Background(new BackgroundFill(color,null,null)));
        SavedData.setBackGroundColor(color);
        setBackGroundColor(color);
    }

    public void setTextColor(Color color){
        this.backGroundColor = color;
        textColorPicker.setValue(color);
        chatLabel.setTextFill(color);

    }

    public void changeTextColor(ActionEvent event){
        Color color = textColorPicker.getValue();
        chatLabel.setTextFill(color);
        SavedData.setTextColor(color);
        setTextColor(color);
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
