package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.User.User;
import com.example.lsmsdb.HelloApplication;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.List;

public class UserController {
    private static User loggedInUser;

    public void userLogout() throws IOException {
        loggedInUser.setLoggedIn(false);
        HelloApplication.changeScene("login.fxml");
    }

    public void userLogin(String username, String fullname, List<Integer> watchlist) throws IOException {
        loggedInUser = new User (username, fullname, watchlist);
        loggedInUser.setLoggedIn(true);
        HelloApplication.changeScene("main-page.fxml");
    }

    public void userRegister() throws IOException {
        HelloApplication.changeScene("register-page.fxml");
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }



}
