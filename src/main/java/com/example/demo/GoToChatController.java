package com.example.demo;

import Client.SavedData;
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
import java.util.Objects;

import static javafx.scene.paint.Color.RED;
import static javafx.scene.paint.Color.GREEN;
public class GoToChatController {

    @FXML
    private TextField username;
    @FXML
    private Label clientWarning;
    public void goToChat(ActionEvent event) throws IOException, InterruptedException {
        SavedData.getClientApp().openPrivateChat(username.getText());
        Thread.sleep(500);
        if(Objects.equals(SavedData.getWarning(), "User Not Found")) {
            //set warning color to red
            clientWarning.setTextFill(RED);
            clientWarning.setText(SavedData.getWarning());
            SavedData.setWarning("");
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Chat.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ChatController chatController = loader.getController();
            chatController.setUsername(username.getText());
            chatController.chatHistory();
            stage.setResizable(false);
            Scene scene = new Scene(root);
            stage.setTitle("Discord");
            stage.setScene(scene);
            stage.show();
        }
    }


}
