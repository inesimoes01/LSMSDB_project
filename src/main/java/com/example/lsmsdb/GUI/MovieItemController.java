package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.Movie.MovieDAO;
import com.example.lsmsdb.Database.User.User;
import com.example.lsmsdb.Database.WatchList.WatchList;
import com.example.lsmsdb.Database.WatchList.WatchListDAO;
import com.example.lsmsdb.HelloApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.List;

public class MovieItemController {

    //TODO make a different movie item for the watchlist
    //TODO button from add to watch list should change to remove from watchlist
    private Movie m;
    @FXML
    public Button changeWatchListButton;
    @FXML
    public Label labelAdded;
    @FXML
    private ImageView poster;
    @FXML
    private Label title;
    @FXML
    private Label year;
    @FXML
    private Label movieid;
    @FXML
    private Label rating;
    @FXML
    private Label genre;
    @FXML
    private Label reviewNumber;

    public void setData(Movie movie){
        m = movie;
        Image posterImage = new Image(movie.getPoster());
        movieid.setText(String.valueOf(movie.getId()));
        poster.setImage(posterImage);
        title.setText(movie.getTitle());
        year.setText(String.valueOf(movie.getYear()));
        rating.setText(String.valueOf(movie.getRating()));
        genre.setText(movie.getGenre());
        reviewNumber.setText(String.valueOf(movie.getReviewNumber()));
        labelAdded.setText(" ");

        if (WatchListDAO.checkIfMovieInWatchList(movie.getId())){
            changeWatchListButton.setText("Remove from WatchList" );
        } else {
            changeWatchListButton.setText("Add to WatchList");
        }

    }
    public void changeMovieInWatchList() throws IOException {
        if (changeWatchListButton.getText().equals("Add to WatchList")){
            addMovieToWatchList();
        } else if (changeWatchListButton.getText().equals("Remove from WatchList")){
            removeMovieFromWatchList();
        }
    }
    public void addMovieToWatchList(){
        WatchList.addMovieToWatchList(Integer.parseInt(movieid.getText()));
        labelAdded.setText("Added");
        changeWatchListButton.setText("Remove from WatchList" );
    }

    public void removeMovieFromWatchList() throws IOException {
        WatchListDAO.removeMovieFromUserWatchList(Integer.parseInt(movieid.getText()));
//        WatchList.removeMovie(Integer.parseInt(movieid.getText()));
        labelAdded.setText("Removed");
        changeWatchListButton.setText("Add to WatchList" );
    }

    public void removeMovieFromWatchListProfile() throws IOException {
        //WatchListDAO.removeMovieFromUserWatchList(User.getUsername(), Integer.parseInt(movieid.getText()));
       WatchList.removeMovie(Integer.parseInt(movieid.getText()));
        labelAdded.setText("Movie removed from your Watch List");
        changeWatchListButton.setText("Add to WatchList" );
    }

    public void goToMoviePage() throws IOException {
        MovieController.setCurrentMovie(m);
        HelloApplication.changeScene("movie-page.fxml");

    }



}
