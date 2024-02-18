package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.StatisticsDAO;
import com.example.lsmsdb.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.lsmsdb.Database.Movie.MovieDAO.getMovieGenre;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Sorts.descending;

public class GeneralStatisticsController {

    @FXML
    private Label statistics2;
    @FXML
    private Label title;
    @FXML
    private Label statistics;
    public static void setAction(String action) {
        GeneralStatisticsController.action = action;
    }
    private static String action;

    public void initialize(){
        switch (action) {
            case "mostAddedMovie" -> {
                // movie in most watchlists
                List<String> movie = StatisticsDAO.getMostAddedMovie();
                statistics.setText("Title: " + movie.get(0));
                statistics2.setText("Number of user with this movie in watchlist: " + movie.get(1));
            }
            case "mostFollowedUser" -> {
                // user with the most following
                List<String> user = StatisticsDAO.getMostFollowedUser();
                statistics.setText("Username: " + user.get(0));
                statistics2.setText("Number of followers: " + user.get(1));
            }
            case "mostCriticalUser" -> {
                // user that gives the lowest ratings
                title.setText("Most Critical User");
                List<String> listUser = StatisticsDAO.getMostCriticalUser();
                if (listUser != null){
                    statistics.setText("Username:" + listUser.get(0));
                    statistics2.setText("Average of the ratings: " + listUser.get(1));
                }

            }
            case "mostTalkedAboutMovies" -> {
                // movies with the highest number of reviews
                List<String> movie = StatisticsDAO.getMostTalkedAboutMovies();
                statistics.setText("Title: " + movie.get(0));
                statistics2.setText("Number of reviews: " + movie.get(1));
            }

            case "mostFamousGenres" -> {
                // genres with the highest number of reviews
                title.setText("Most Famous Genres");
                List<String> genre = StatisticsDAO.getMostFamousGenres();
                List<Integer> listids = new ArrayList<>();
                listids.add(Integer.valueOf(genre.get(0)));
                statistics.setText("Genre: " + getMovieGenre(listids));
                statistics2.setText("Number of reviews: " + genre.get(1));
            }

        }
    }

    @FXML
    void goToMainPage(ActionEvent event) throws IOException {
        HelloApplication.changeScene("main-page.fxml");

    }
}
