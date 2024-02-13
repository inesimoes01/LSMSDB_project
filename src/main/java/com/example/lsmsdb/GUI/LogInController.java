package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.DatabaseMongoDB;
import com.example.lsmsdb.Database.User.UserDAO;
import com.example.lsmsdb.Database.WatchList.WatchListDAO;
import com.example.lsmsdb.HelloApplication;
import com.example.lsmsdb.Database.User.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LogInController {

    @FXML
    private Button login;
    @FXML
    private Button register;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label wrongLogin;


    @FXML
    private void userLogin(ActionEvent event) throws IOException{
        UserController u = new UserController();
        if (UserDAO.checkUsernameCredentials(username.getText(), password.getText())){
            u.userLogin();
            WatchListDAO.initializeWatchList();
        }else if(username.getText().isEmpty() || password.getText().isEmpty()){
            wrongLogin.setText("Please enter the data");
        }else wrongLogin.setText("Wrong credentials");
    }

    @FXML
    private void userRegister(ActionEvent event) throws IOException{
        UserController u = new UserController();
        u.userRegister();
    }



}
