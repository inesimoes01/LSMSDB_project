package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.Review.Review;
import com.example.lsmsdb.Database.Review.ReviewDAO;
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
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class MoviePageController {

    @FXML
    private Button changeWatchListButton;

    @FXML
    private Label genre;

    @FXML
    private Label overview;

    @FXML
    private ImageView poster;

    @FXML
    private Label rating;

    @FXML
    private Button reviewMovieButton;

    @FXML
    private Label reviewNumber;

    @FXML
    private VBox reviewVBox;

    @FXML
    private Label title;

    @FXML
    private Label year;
    @FXML
    private Label movieid;

    public void initialize(){
        Movie curr = MovieController.getCurrentMovie();
        genre.setText(curr.getGenre());
        overview.setText(curr.getOverview());

        Image posterImage = new Image(curr.getPoster());
        poster.setImage(posterImage);

        rating.setText(String.valueOf(curr.getRating()));
        reviewNumber.setText(String.valueOf(curr.getReviewNumber()));
        movieid.setText(String.valueOf(curr.getId()));
        title.setText(curr.getTitle());
        year.setText(String.valueOf(curr.getYear()));

        if (WatchListDAO.checkIfMovieInWatchList(curr.getId())){
            changeWatchListButton.setText("Remove from WatchList" );
        } else {
            changeWatchListButton.setText("Add to WatchList");
        }

        //System.out.println(MovieController.getCurrentMovie().getId() + MovieController.getCurrentMovie().getTitle());

        List<Review> reviewList = ReviewDAO.getReviewsFromMovie(MovieController.getCurrentMovie().getId());
        displayReview(reviewList);
    }

    @FXML
    void addReviewToMovie(ActionEvent event) {

    }

    @FXML
    void changeMovieInWatchList(ActionEvent event) {

    }

    @FXML
    void goToMoviePage(TouchEvent event) {

    }

    public void displayReview(List<Review> reviewList) {
        // Clear existing movie items
        reviewVBox.getChildren().clear();

        // Load movie items in a background thread
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (Review review : reviewList) {
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("review-item.fxml"));
                    try {
                        HBox grid = fxmlLoader.load();
                        ReviewItemController ri = fxmlLoader.getController();
                        ri.setData(review);
                        //System.out.println("filled the review");
                        // Add the movie item to the VBox
                        Platform.runLater(() -> reviewVBox.getChildren().add(grid));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        return null;
            }
        };

         //Start the background task
        new Thread(task).start();
    }

    public void goToMainPage(ActionEvent actionEvent) throws IOException {
        HelloApplication.changeScene("main-page.fxml");
    }
}
