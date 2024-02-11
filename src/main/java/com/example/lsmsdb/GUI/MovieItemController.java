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

public class MovieItemController {

    //TODO make a different movie item for the watchlist
    //TODO button from add to watch list should change to remove from watchlist
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
    private Movie m;

    public void setData(Movie movie){
        Image posterImage = new Image(movie.getPoster());
        movieid.setText(String.valueOf(movie.getId()));
        poster.setImage(posterImage);
        title.setText(movie.getTitle());
        year.setText(String.valueOf(movie.getYear()));
        rating.setText(String.valueOf(movie.getRating()));
        genre.setText(movie.getGenre());
        labelAdded.setText(" ");
        if (WatchListDAO.checkIfMovieInWatchList(User.getUsername(), movie.getId())){
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
        labelAdded.setText("Movie added to your Watch List");
        changeWatchListButton.setText("Remove from WatchList" );
    }

    public void removeMovieFromWatchList() throws IOException {
        WatchListDAO.removeMovieFromUserWatchList(User.getUsername(), Integer.parseInt(movieid.getText()));
//        WatchList.removeMovie(Integer.parseInt(movieid.getText()));
        labelAdded.setText("Movie removed from your Watch List");
        changeWatchListButton.setText("Add to WatchList" );
    }

    public void removeMovieFromWatchListProfile() throws IOException {
        //WatchListDAO.removeMovieFromUserWatchList(User.getUsername(), Integer.parseInt(movieid.getText()));
       WatchList.removeMovie(Integer.parseInt(movieid.getText()));
        labelAdded.setText("Movie removed from your Watch List");
        changeWatchListButton.setText("Add to WatchList" );
    }

    public void goToMoviePage() throws IOException {
        HelloApplication.changeScene("movie-page.fxml");
    }



}
