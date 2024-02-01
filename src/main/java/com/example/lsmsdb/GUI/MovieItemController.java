package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.Movie.MovieDAO;
import com.example.lsmsdb.Database.WatchList.WatchList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MovieItemController {

    //TODO make a different movie item for the watchlist
    //TODO button from add to watch list should change to remove from watchlist
    @FXML
    public Button addToWatchListButton;
    @FXML
    private ImageView poster;
    @FXML
    private Label title;
    @FXML
    private Label year;
    private Movie m;

    public void setData(Movie movie){
        Image posterImage = new Image(movie.getPoster());
        poster.setImage(posterImage);
        title.setText(movie.getTitle());
        year.setText(String.valueOf(movie.getYear()));
    }

    public void addMovieToWatchList(){
        MovieDAO mD = new MovieDAO();
        WatchList.addMovieToWatchList(mD.getMovieFromName(String.valueOf(title)));
    }


}
