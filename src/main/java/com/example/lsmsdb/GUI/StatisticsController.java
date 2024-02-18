package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.StatisticsDAO;
import com.example.lsmsdb.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.List;

public class StatisticsController {


    @FXML
    private Hyperlink mostAddedMovie;

    @FXML
    private Hyperlink mostCriticalUser;

    @FXML
    private Hyperlink mostFamousGenres;

    @FXML
    private Hyperlink mostFollowedUser;

    @FXML
    private Hyperlink mostTalkedAboutMovies;

    @FXML
    private Hyperlink mostVersatileUser;

    @FXML
    void goToMostAddedMovie(ActionEvent event) throws IOException {
        GeneralStatisticsController.setAction("mostAddedMovie");
        HelloApplication.changeScene("general-statistics.fxml");
    }

    @FXML
    void goToMostCriticalUser(ActionEvent event) throws IOException {
        GeneralStatisticsController.setAction("mostCriticalUser");
        HelloApplication.changeScene("general-statistics.fxml");
    }

    @FXML
    void goToMostFamousGenres(ActionEvent event) throws IOException {
        GeneralStatisticsController.setAction("mostFamousGenres");
        HelloApplication.changeScene("general-statistics.fxml");
    }

    @FXML
    void goToMostFollowedUser(ActionEvent event) throws IOException {
        GeneralStatisticsController.setAction("mostFollowedUser");
        HelloApplication.changeScene("general-statistics.fxml");
    }

    @FXML
    void goToMostTalkedAboutMovies(ActionEvent event) throws IOException {
        GeneralStatisticsController.setAction("mostTalkedAboutMovies");
        HelloApplication.changeScene("general-statistics.fxml");
    }

    @FXML
    void goToMostVersatileUser(ActionEvent event) throws IOException {
        GeneralStatisticsController.setAction("mostVersatileUser");
        HelloApplication.changeScene("general-statistics.fxml");
    }

    @FXML
    void goToMainPage(ActionEvent event) throws IOException {
        HelloApplication.changeScene("main-page.fxml");

    }

}

//Select users that have the highest number of categories in their reading list
//Select papers with the highest number of comments.
//categories ordered by the number of papers published.
//categories ordered by the number of comments.