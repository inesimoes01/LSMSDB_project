package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.Movie.MovieDAO;
import com.example.lsmsdb.Database.StatisticsDAO;
import com.example.lsmsdb.HelloApplication;
import com.example.lsmsdb.Database.User.User;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainPageController {
    @FXML
    private Hyperlink logoutLink;

    @FXML
    private Hyperlink mainPageLink;

    @FXML
    private VBox movieVBOX;

    @FXML
    private Hyperlink newMoviesLink;

    @FXML
    private Hyperlink newUsersLink;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchText;

    @FXML
    private Hyperlink usernameLink;

    @FXML
    private Hyperlink statisticsLink;
    public void initialize(){
        usernameLink.setText(UserController.getLoggedInUser().getFullName());
        List<Movie> movieList = MovieDAO.getMainPageMovies();
        System.out.println("alo");
        displayMovies(movieList);
    }

    @FXML
    private void goToProfile(ActionEvent event) throws IOException {
        cancelMovieLoading();
        HelloApplication.changeScene("profile-page.fxml");
    }

    @FXML
    void goToNewMovies(ActionEvent event) throws IOException {
        cancelMovieLoading();
        FollowListController.setAction("suggestBasedOnMovie");
        HelloApplication.changeScene("follow-list.fxml");
    }

    @FXML
    void goToNewUsers(ActionEvent event) throws IOException {
        cancelMovieLoading();
        FollowListController.setAction("suggestBasedOnUser");
        HelloApplication.changeScene("follow-list.fxml");
    }

    @FXML
    void goToMainPage(ActionEvent event) {
        cancelMovieLoading();
        initialize();
    }

    @FXML
    private void userLogout(ActionEvent event) throws IOException {
        cancelMovieLoading();
        UserController u = new UserController();
        u.userLogout();
    }

    @FXML
    public void searchMovie(ActionEvent event) {
        cancelMovieLoading();
        List<Movie> movieList = MovieDAO.getMoviesFromName(searchText.getText());
        displayMovies(movieList);
    }

    @FXML
    public void goToStatistics() throws IOException {
        cancelMovieLoading();
        HelloApplication.changeScene("statistics.fxml");
    }



    private static Thread movieLoadingThread;

    public void displayMovies(List<Movie> movieList) {
        // Clear existing movie items
        movieVBOX.getChildren().clear();

        // Create a new task
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (Movie movie : movieList) {
                    if (isCancelled()) { // Check if the task is cancelled
                        break; // Exit the loop if the task is cancelled
                    }
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("movie-item.fxml"));
                    try {
                        HBox grid = fxmlLoader.load();
                        MovieItemController mi = fxmlLoader.getController();
                        mi.setData(movie);
                        // Add the movie item to the VBox
                        Platform.runLater(() -> movieVBOX.getChildren().add(grid));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }
                }
                return null;
            }
        };

        // Start the background task
        movieLoadingThread = new Thread(task);
        movieLoadingThread.start();
    }
    public static void cancelMovieLoading() {
        if (movieLoadingThread != null) {
            movieLoadingThread.interrupt(); // Interrupt the thread

        }
    }

}
//    public void displayMovies(List<Movie> movieList) {
//        // Clear existing movie items
//        movieVBOX.getChildren().clear();
//
//        // Load movie items in a background thread
//        Task<Void> task = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                for (Movie movie : movieList) {
//                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("movie-item.fxml"));
//                    try {
//                        HBox grid = fxmlLoader.load();
//                        MovieItemController mi = fxmlLoader.getController();
//                        mi.setData(movie);
//                        // Add the movie item to the VBox
//                        Platform.runLater(() -> movieVBOX.getChildren().add(grid));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return null;
//            }
//        };
//
//        // Start the background task
//        new Thread(task).start();
//    }