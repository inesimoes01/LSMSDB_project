package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.Review.Review;
import com.example.lsmsdb.Database.Review.ReviewDAO;
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
    private Label labelAdded;

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
        labelAdded.setText(" ");
        if (UserController.checkIfMovieInWatchList(movieid.getText())){
            changeWatchListButton.setText("Remove from WatchList" );
        } else {
            changeWatchListButton.setText("Add to WatchList");
        }
        //System.out.println(MovieController.getCurrentMovie().getId() + MovieController.getCurrentMovie().getTitle());

        List<Review> reviewList = ReviewDAO.getReviewsFromMovie(MovieController.getCurrentMovie().getId());
        System.out.println(reviewList);
        displayReview(reviewList);
    }

    @FXML
    void addReviewToMovie(ActionEvent event) throws IOException {
        HelloApplication.changeScene("add-review.fxml");
    }

    @FXML
    void changeMovieInWatchList(ActionEvent event) throws IOException {
        if (changeWatchListButton.getText().equals("Add to WatchList")){
            addMovieToWatchList();
        } else if (changeWatchListButton.getText().equals("Remove from WatchList")){
            removeMovieFromWatchList();
        }
    }

    @FXML
    void goToMoviePage(TouchEvent event) {

    }

    public void changeMovieInWatchList() throws IOException {

    }
    public void addMovieToWatchList(){
        WatchList.addMovieToWatchList(movieid.getText(), title.getText(), getImageUrl(poster));
        labelAdded.setText("Added");
        UserController.updateWatchList();
        changeWatchListButton.setText("Remove from WatchList" );
    }

    public static String getImageUrl(ImageView imageView) {
        Image image = imageView.getImage();
        if (image != null) {
            return image.getUrl();
        }
        return null; // If image is not set
    }

    public void removeMovieFromWatchList() throws IOException {
        //WatchList.removeMovie(movieid.getText());
        WatchList.removeMovieMain(movieid.getText());
        labelAdded.setText("Removed");
        UserController.updateWatchList();
        changeWatchListButton.setText("Add to WatchList" );
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
                        // Add the movie item to the VBox
                        Platform.runLater(() -> reviewVBox.getChildren().add(grid));
                    } catch (IOException e) {
                        System.out.println(e);
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
