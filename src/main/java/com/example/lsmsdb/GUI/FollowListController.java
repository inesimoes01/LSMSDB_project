package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.User.UserDAO;
import com.example.lsmsdb.HelloApplication;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class FollowListController {
    public static void setAction(String action) {
        FollowListController.action = action;
    }

    private static String action;

    @FXML
    private VBox followVBox;

    @FXML
    private Button goBackButton;

    @FXML
    private Label titleLabel;

    public void initialize(){
        titleLabel.setText("Following List");
        System.out.println("Action: " + action);
        switch (action) {
            case "myfollowers" -> {
                titleLabel.setText("Followers List");
                List<String> l = UserDAO.getUserFollowers(UserController.getLoggedInUser().getUsername());
                if (l != null) {
                    displayFollower(l);
                }
            }
            case "myfollowing" -> {
                titleLabel.setText("Following List");
                List<String> l = UserDAO.getUserFollowing(UserController.getLoggedInUser().getUsername());
                System.out.println(l);
                if (l != null) {
                    displayFollower(l);
                }
            }
            case "followers" -> {
                titleLabel.setText("Followers List");
                List<String> l = UserDAO.getUserFollowers(UserProfilePageController.getCurr().getUsername());
                System.out.println("list" + l + " name "+ UserProfilePageController.getCurr().getUsername());
                if (l != null) {
                    displayFollower(l);
                }
            }
            case "following" -> {
                titleLabel.setText("Following List");
                List<String> l = UserDAO.getUserFollowing(UserProfilePageController.getCurr().getUsername());
                if (l != null) {
                    displayFollower(l);
                }
            }
        }

    }


    @FXML
    void goToMainPage(ActionEvent event) throws IOException {
        HelloApplication.changeScene("main-page.fxml");

    }

    private static Thread movieLoadingThread;

    public void displayFollower(List<String> followList) {
        // Clear existing movie items
        followVBox.getChildren().clear();

        // Create a new task
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (String user : followList) {
                    if (isCancelled()) { // Check if the task is cancelled
                        break; // Exit the loop if the task is cancelled
                    }
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("follow-item.fxml"));
                    try {
                        HBox grid = fxmlLoader.load();
                        FollowItemController mi = fxmlLoader.getController();
                        mi.setData(user);
                        // Add the movie item to the VBox
                        Platform.runLater(() -> followVBox.getChildren().add(grid));
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
