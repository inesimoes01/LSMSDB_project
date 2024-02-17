package com.example.lsmsdb.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FollowItemController {

    @FXML
    private Label usernameLabel;

    public void setData(String user){
        usernameLabel.setText(user);
    }
}
