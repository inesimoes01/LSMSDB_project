package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.User.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;

public class RegisterPageController {
    @FXML
    private Button register;
    @FXML
    private TextField username;
    @FXML
    private TextField fullname;
    @FXML
    private PasswordField password;
    @FXML
    private Label wrongRegister;

    @FXML
    private void userRegister(ActionEvent event) throws IOException{
        UserController u = new UserController();
        // check if user exists
        if(username.getText().isEmpty() || password.getText().isEmpty() || fullname.getText().isEmpty()){
            wrongRegister.setText("Username already exists");
        }else if (UserDAO.checkUsernameExists(username.getText())){
            wrongRegister.setText("Please enter the data");
        }else if (UserDAO.createUser(username.getText(), password.getText(), fullname.getText())){
            u.userLogin(username.getText(), fullname.getText(), new ArrayList<>());
        }

    }
}
