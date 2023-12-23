package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.DatabaseMongoDB;
import com.example.lsmsdb.Database.User.UserDAO;
import com.example.lsmsdb.HelloApplication;
import com.example.lsmsdb.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LogIn {

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
        checkLogin();
    }

    @FXML
    private void userRegister(ActionEvent event) throws IOException{
        checkLogin();
    }

    private void checkLogin() throws IOException {
        HelloApplication m = new HelloApplication();
        DatabaseMongoDB db = new DatabaseMongoDB();
        if (UserDAO.checkUsernameCredentials(username.getText(), password.getText())){
            m.changeScene("main-page.fxml");
            User.setLoggedIn(true);
        }else if(username.getText().isEmpty() && password.getText().isEmpty()){
            wrongLogin.setText("Please enter the data");
        }else wrongLogin.setText("Wrong credentials");
    }
}
