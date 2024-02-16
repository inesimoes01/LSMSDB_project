package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.User.User;
import com.example.lsmsdb.Database.WatchList.WatchList;
import com.example.lsmsdb.Database.WatchList.WatchListDAO;
import com.example.lsmsdb.HelloApplication;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class ProfilePageController {

    @FXML
    private Button backButton;

    @FXML
    private Hyperlink logOutLink;

    @FXML
    private ImageView profileImage;

    @FXML
    private Label textWithName;

    @FXML
    private VBox watchListVBOX;


    @FXML
    void goToMainPage(ActionEvent event) throws IOException {
        cancelMovieLoading();
        HelloApplication.changeScene("main-page.fxml");
    }

    @FXML
    private void userLogout(ActionEvent event) throws IOException {
        UserController u = new UserController();
        cancelMovieLoading();
        u.userLogout();
    }

    public void initialize(){
        textWithName.setText("Hello " + UserController.getLoggedInUser().getFullName() + "!");
        Image profile = new Image(UserController.getLoggedInUser().getProfileImage());
        profileImage.setImage(profile);
        displayWatchList();
    }

    private void displayWatchList(){
        List<Movie> movieList = UserController.getLoggedInUser().getWatchlistMovie();
//        System.out.println("movie list:" + movieList);
        System.out.println("user logged movie list " + UserController.getLoggedInUser().getWatchlistMovie());
        if (!movieList.isEmpty()){
            displayMovies(movieList);
        }

    }
    private static Thread movieLoadingThread;

    public void displayMovies(List<Movie> movieList) {
        // Clear existing movie items
        watchListVBOX.getChildren().clear();

        // Create a new task
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (Movie movie : movieList) {
                    if (isCancelled()) { // Check if the task is cancelled
                        break; // Exit the loop if the task is cancelled
                    }
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("movie-item-profile.fxml"));
                    try {
                        HBox grid = fxmlLoader.load();
                        MovieItemProfileController mi = fxmlLoader.getController();
                        mi.setData(movie);
                        // Add the movie item to the VBox
                        Platform.runLater(() -> watchListVBOX.getChildren().add(grid));
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

//    public void displayMovies(List<Movie> movieList) {
//        watchListVBOX.getChildren().clear();
//
//        Task<Void> task = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                for (Movie movie : movieList) {
//                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("movie-item-profile.fxml"));
//                    try {
//                        HBox grid = fxmlLoader.load();
//                        MovieItemController mi = fxmlLoader.getController();
//                        mi.setData(movie);
//
//                        // Add the movie item to the VBox
//                        Platform.runLater(() -> watchListVBOX.getChildren().add(grid));
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
}

