package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.User.User;
import com.example.lsmsdb.HelloApplication;
import javafx.event.ActionEvent;

import java.io.IOException;

public class UserController {
    public void userLogout() throws IOException {
        User.setLoggedIn(false);
        HelloApplication m = new HelloApplication();
        m.changeScene("login.fxml");
    }

    public void userLogin() throws IOException {
        HelloApplication m = new HelloApplication();
        m.changeScene("main-page.fxml");
        User.setLoggedIn(true);
    }
}
