package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WarningController {
    @FXML
    private Label messageLabel;

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

}
