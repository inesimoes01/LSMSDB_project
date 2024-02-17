package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.WatchList.WatchList;
import com.example.lsmsdb.HelloApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class MovieItemUserController {
    private Movie m;
    @FXML
    private ImageView poster;
    @FXML
    private Label title;
    @FXML
    private Label movieid;
    @FXML
    private Button changeWatchListButton;

    @FXML
    private Label labelAdded;

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


        //Image posterImage = new Image(movie.getPosterURL());
        movieid.setText(String.valueOf(movie.getId()));
        //poster.setImage(posterImage);
        title.setText(movie.getTitle());
        labelAdded.setText(" ");

        if (UserController.checkIfMovieInWatchList(movie.getId())){
            changeWatchListButton.setText("Remove from WatchList" );
        } else {
            changeWatchListButton.setText("Add to WatchList");
        }
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

    public void changeMovieInWatchList() throws IOException {
        if (changeWatchListButton.getText().equals("Add to WatchList")){
            addMovieToWatchList();
        } else if (changeWatchListButton.getText().equals("Remove from WatchList")){
            removeMovieFromWatchList();
        }
    }
    public void addMovieToWatchList(){
        WatchList.addMovieToWatchList(movieid.getText(), title.getText(), getImageUrl(poster));
        labelAdded.setText("Added");
        UserController.updateWatchList();
        changeWatchListButton.setText("Remove from WatchList" );
    }

    public void removeMovieFromWatchList() throws IOException {
        //WatchList.removeMovie(movieid.getText());
        WatchList.removeMovieMain(movieid.getText());
        labelAdded.setText("Removed");
        UserController.updateWatchList();
        changeWatchListButton.setText("Add to WatchList" );
    }
}
