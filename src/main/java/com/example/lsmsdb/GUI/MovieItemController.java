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
    private Button changeWatchListButton;

    @FXML
    private Label genre;

    @FXML
    private Label labelAdded;

    @FXML
    private Label movieid;

    @FXML
    private ImageView poster;

    @FXML
    private Label rating;

    @FXML
    private Label reviewNumber;

    @FXML
    private Label title;

    @FXML
    private Label year;

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


        title.setText(movie.getTitle());
        genre.setText(movie.getGenre());
        rating.setText(String.valueOf(movie.getRating()));
        reviewNumber.setText(String.valueOf(movie.getReviewNumber()));
        year.setText(String.valueOf(movie.getYear()));
        labelAdded.setText(" ");

        if (UserController.checkIfMovieInWatchList(movie.getId())){
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

//    public void removeMovieFromWatchListProfile() throws IOException {
//        //WatchListDAO.removeMovieFromUserWatchList(User.getUsername(), Integer.parseInt(movieid.getText()));
//       WatchList.removeMovie(movieid.getText());
//        labelAdded.setText("Movie removed from your Watch List");
//        changeWatchListButton.setText("Add to WatchList" );
//    }

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
