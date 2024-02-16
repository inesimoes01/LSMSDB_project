package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.User.User;
import com.example.lsmsdb.Database.User.UserDAO;
import com.example.lsmsdb.Database.WatchList.WatchList;
import com.example.lsmsdb.Database.WatchList.WatchListDAO;
import com.example.lsmsdb.HelloApplication;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class UserProfilePageController {
    private static User curr;

    @FXML
    private Button backButton;

    @FXML
    private Label fullname;

    @FXML
    private ImageView profileImage;

    @FXML
    private VBox watchListVBox;

    public static void setData(User u){
        curr = u;
    }

    public void initialize(){
        fullname.setText(curr.getFullName());

        if (curr.getProfileImage() != null){
            Image profile = new Image(curr.getProfileImage());
            profileImage.setImage(profile);
        }
        List<Movie> m =  curr.getWatchlistMovie();

        if (m != null){
            System.out.println("movies to displau" + m);
            displayMovies(m);
        }

    }
    @FXML
    void goToMainPage(ActionEvent event) throws IOException {
        HelloApplication.changeScene("main-page.fxml");
    }

    @FXML
    void followUser(ActionEvent event) {
        UserDAO.followUser();
    }

    public void displayMovies(List<Movie> movieList) {
        // Clear existing movie items
        watchListVBox.getChildren().clear();

        // Load movie items in a background thread
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (Movie movie : movieList) {
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("movie-item-user.fxml"));
                    try {
                        HBox grid = fxmlLoader.load();
                        MovieItemUserController mi = fxmlLoader.getController();
                        System.out.println("ajhhh");
                        mi.setData(movie);
                        System.out.println("finished");

                        // Add the movie item to the VBox
                        Platform.runLater(() -> watchListVBox.getChildren().add(grid));
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
