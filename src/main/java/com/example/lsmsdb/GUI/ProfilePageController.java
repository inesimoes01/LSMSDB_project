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
        HelloApplication.changeScene("main-page.fxml");
    }

    @FXML
    private void userLogout(ActionEvent event) throws IOException {
        UserController u = new UserController();
        u.userLogout();
    }

    public void initialize(){
        textWithName.setText("Hello " + UserController.getLoggedInUser().getFullName() + "!");
        //profileImage.setImage(User.getProfilePic());
        displayWatchList();
    }

    private void displayWatchList(){
        List<Movie> movieList = WatchListDAO.getMoviesFromWatchList();
        if (!movieList.isEmpty()){
            displayMovies(movieList);
        }
    }

    public void displayMovies(List<Movie> movieList) {
        watchListVBOX.getChildren().clear();

        watchListVBOX.getChildren().add(new Label("Loading..."));

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (Movie movie : movieList) {
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("movie-item-profile.fxml"));
                    try {
                        HBox grid = fxmlLoader.load();
                        MovieItemController mi = fxmlLoader.getController();
                        mi.setData(movie);

                        // Add the movie item to the VBox
                        Platform.runLater(() -> watchListVBOX.getChildren().add(grid));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };

        // Start the background task
        new Thread(task).start();
    }
}

