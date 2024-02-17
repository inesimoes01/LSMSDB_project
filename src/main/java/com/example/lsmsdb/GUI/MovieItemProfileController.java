package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.User.User;
import com.example.lsmsdb.Database.WatchList.WatchList;
import com.example.lsmsdb.Database.WatchList.WatchListDAO;
import com.example.lsmsdb.HelloApplication;
import com.example.lsmsdb.HelloController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class MovieItemProfileController {

    private Movie m;
    @FXML
    public Button changeWatchListButton;
    @FXML
    private ImageView poster;
    @FXML
    private Label title;
    @FXML
    private Label movieid;

    public void setData(Movie movie){
        m = movie;

        if(movie.getPoster().contains("https://")){
            Image posterImage = new Image(movie.getPoster());
            movieid.setText(String.valueOf(movie.getId()));
            poster.setImage(posterImage);
        }else {
            Image posterImage = new Image(movie.getPosterURL());
            movieid.setText(String.valueOf(movie.getId()));
            poster.setImage(posterImage);
        }

//        Image posterImage = new Image(movie.getPoster());
        movieid.setText(String.valueOf(movie.getId()));
//        poster.setImage(posterImage);
        title.setText(movie.getTitle());

        changeWatchListButton.setText("Remove from WatchList" );

    }

    public void removeMovieFromWatchListProfile() throws IOException {
        System.out.println("before updating "+ UserController.getLoggedInUser().getWatchlistMovie().size());
        UserController.removeWatchList(movieid.getText());
       WatchList.removeMovieProfile();
       System.out.println("after updating "+ UserController.getLoggedInUser().getWatchlistMovie().size());

       changeWatchListButton.setText("Add to WatchList" );
    }

    public void goToMoviePage() throws IOException {
        MainPageController.cancelMovieLoading();
        MovieController.setCurrentMovie(m);
        HelloApplication.changeScene("movie-page.fxml");

    }

    public static String getImageUrl(ImageView imageView) {
        Image image = imageView.getImage();
        if (image != null) {
            return image.getUrl();
        }
        return null; // If image is not set
    }



}
