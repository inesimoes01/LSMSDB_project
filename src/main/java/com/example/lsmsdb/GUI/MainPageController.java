package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.Movie.MovieDAO;
import com.example.lsmsdb.HelloApplication;
import com.example.lsmsdb.Database.User.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainPageController {
    public TextField searchText;
    public Button searchButton;
    public Hyperlink newMoviesLink;
    public Hyperlink newUsersLink;
    @FXML
    private Hyperlink profileLink;
    @FXML
    private Hyperlink recommendationsLink;
    @FXML
    private Hyperlink logoutLink;
    @FXML
    private Label usernameLabel;
    @FXML
    private ImageView profilePicture;

    @FXML
    private VBox movieVBOX;

    public void initialize(){

        usernameLabel.setText(User.getFullName());
        List<Movie> movieList = MovieDAO.getListMovies();
        displayMovies(movieList);
    }
    @FXML
    private void goToProfile(ActionEvent event) throws IOException {
        HelloApplication.changeScene("profile-page.fxml");
    }

    @FXML
    private void goToRecommendations(ActionEvent event){

    }

    @FXML
    private void userLogout(ActionEvent event) throws IOException {
        UserController u = new UserController();
        u.userLogout();
    }

    public void searchMovie(ActionEvent event) {
        List<Movie> movieList = MovieDAO.getMoviesFromName(searchText.getText());
        displayMovies(movieList);
    }

    public void displayMovies(List<Movie> movieList){
        movieVBOX.getChildren().clear();

        for (Movie movie : movieList){
            System.out.println("Movie: " + movie.getTitle());
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("movie-item.fxml"));
            try {
                HBox grid = fxmlLoader.load();
                MovieItemController mi = fxmlLoader.getController();
                mi.setData(movie);
                movieVBOX.getChildren().add(grid);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }


//    private List<Movie> getSearchResults(){
//        List<Movie> movieList = new ArrayList<>();
//        Movie m1 = new Movie("Parasite", 2019, "https://m.media-amazon.com/images/I/81hsswTfQXL._AC_UF894,1000_QL80_.jpg");
//        Movie m2 = new Movie("Nemo", 2009, "https://m.media-amazon.com/images/I/81hsswTfQXL._AC_UF894,1000_QL80_.jpg");
//        Movie m3 = new Movie("Pulp Fiction", 1990, "https://m.media-amazon.com/images/M/MV5BNGNhMDIzZTUtNTBlZi00MTRlLWFjM2ItYzViMjE3YzI5MjljXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg");
//        Movie m4 = new Movie("Fight Club", 2001, "https://m.media-amazon.com/images/I/81hsswTfQXL._AC_UF894,1000_QL80_.jpg");
//        Movie m5 = new Movie("Hannah Montana", 2014, "https://m.media-amazon.com/images/M/MV5BYjZhY2NiN2EtMjMwZi00YTZlLTljOTktYTExNjliZTQ5ZTMxXkEyXkFqcGdeQXVyODk1MjAxNzQ@._V1_FMjpg_UX1000_.jpg");
//        Movie m6 = new Movie("500 Days of Summer", 2016, "https://m.media-amazon.com/images/I/81hsswTfQXL._AC_UF894,1000_QL80_.jpg");
//
//
//        movieList.add(m1);
//        movieList.add(m2);
//        movieList.add(m3);
//        movieList.add(m4);
//        movieList.add(m5);
//        movieList.add(m6);
//
//        return movieList;
//    }

    public void goToNewMovies(ActionEvent actionEvent) {
    }

    public void goToNewUsers(ActionEvent actionEvent) {
    }
}
