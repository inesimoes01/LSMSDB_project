package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.User.User;
import com.example.lsmsdb.HelloApplication;
import javafx.event.ActionEvent;

import java.io.IOException;

public class UserController {
    public void userLogout() throws IOException {
        User.setLoggedIn(false);
        HelloApplication.changeScene("login.fxml");
    }

    public void userLogin() throws IOException {
        HelloApplication.changeScene("main-page.fxml");
        User.setLoggedIn(true);
    }

    public void userRegister() throws IOException {
        HelloApplication.changeScene("register-page.fxml");
    }


}
