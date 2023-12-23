package com.example.lsmsdb.GUI;

import com.example.lsmsdb.HelloApplication;
import com.example.lsmsdb.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.TouchEvent;

import java.io.IOException;

public class MainPage {
    public TextField searchText;
    public Button searchButton;
    @FXML
    private Hyperlink profileLink;
    @FXML
    private Hyperlink recommendationsLink;
    @FXML
    private Hyperlink logoutLink;
    @FXML
    private Label usernameLabel;
    @FXML
    private ImageView profilePicture;

    @FXML
    private ImageView book1Image;
    @FXML
    private ImageView book2Image;
    @FXML
    private ImageView book3Image;
    @FXML
    private ImageView book4Image;
    @FXML
    private Label book1Name;
    @FXML
    private Label book2Name;
    @FXML
    private Label book3Name;
    @FXML
    private Label book4Name;

    @FXML
    private void goToProfile(ActionEvent event) throws IOException {
        HelloApplication m = new HelloApplication();
        m.changeScene("profile-page.fxml");
    }

    @FXML
    private void goToRecommendations(ActionEvent event){

    }

    @FXML
    private void userLogout(ActionEvent event) throws IOException {
        User.setLoggedIn(false);
        HelloApplication m = new HelloApplication();
        m.changeScene("login-page.fxml");
    }


    public void goToMovie(TouchEvent touchEvent) throws IOException {
        HelloApplication m = new HelloApplication();
        m.changeScene("movie-page.fxml");
    }

    public void searchMovie(ActionEvent event) {
        Image img = new Image("file:///C:/Users/mines/OneDrive - Universidade do Porto/imagens/avatar/994598078f8b1ee0c74d1ae3f948534e.jpg");
        book1Image.setImage(img);
        book2Image.setImage(img);
        book3Image.setImage(img);
        book4Image.setImage(img);

        book1Name.setText("one");
        book2Name.setText("two");
        book3Name.setText("three");
        book4Name.setText("four");
    }
}
