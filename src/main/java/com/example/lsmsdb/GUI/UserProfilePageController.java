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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class UserProfilePageController {
    public static User getCurr() {
        return curr;
    }

    private static User curr;

    @FXML
    private Button backButton;

    @FXML
    private Label fullname;

    @FXML
    private ImageView profileImage;

    @FXML
    private VBox watchListVBox;
    @FXML
    private Button followButton;
    @FXML
    private Hyperlink followersLink;

    @FXML
    private Hyperlink followingLink;

    public static void setData(User u){
        curr = u;
    }

    public void initialize(){

        //check if user follows this person
        List<String> l = UserDAO.getUserFollowing(UserController.getLoggedInUser().getUsername());

        if (l.contains(curr.getUsername())){
            followButton.setText("Unfollow");
        } else followButton.setText("Follow");

        fullname.setText(curr.getFullName());

        if (curr.getProfileImage() != null){
            Image profile = new Image(curr.getProfileImage());
            profileImage.setImage(profile);
        }
        List<Movie> m =  curr.getWatchlistMovie();

        if (m != null){
            displayMovies(m);
        }

    }
    @FXML
    void goToMainPage(ActionEvent event) throws IOException {
        HelloApplication.changeScene("main-page.fxml");
    }

    @FXML
    void followUser(ActionEvent event) {
        if (followButton.getText().equals("Follow")){
            UserDAO.followUser(curr.getUsername());
            followButton.setText("Unfollow");
        }
        else if(followButton.getText().equals("Unfollow")){
            UserDAO.unfollowUser(curr.getUsername());
            followButton.setText("Follow");
        }
    }

    @FXML
    void goToFollowersPage(ActionEvent event) throws IOException {
        FollowListController.setAction("followers");
        HelloApplication.changeScene("follow-list.fxml");
    }

    @FXML
    void goToFollowingPage(ActionEvent event) throws IOException {
        FollowListController.setAction("following");
        HelloApplication.changeScene("follow-list.fxml");
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

                        mi.setData(movie);

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
