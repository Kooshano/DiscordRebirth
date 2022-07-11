package com.example.demo;

import Client.ClientApp;
import Client.SavedData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class DiscordApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SignInPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ClientApp clientApp = new ClientApp();
        SavedData.setClientApp(clientApp);
        stage.setResizable(false);
        stage.setTitle("Discord");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}