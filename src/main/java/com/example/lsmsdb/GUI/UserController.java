package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.User.User;
import com.example.lsmsdb.Database.WatchList.WatchListDAO;
import com.example.lsmsdb.HelloApplication;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.List;

public class UserController {
    private static User loggedInUser;

    public void userLogout() throws IOException {
        loggedInUser.setLoggedIn(false);
        HelloApplication.changeScene("login.fxml");
    }

    public void userLogin(String username, String fullname, String profilepic, List<Movie> watchlist) throws IOException {
        loggedInUser = new User (username, fullname, profilepic, watchlist);
        loggedInUser.setLoggedIn(true);
        HelloApplication.changeScene("main-page.fxml");
    }

    public void userRegister() throws IOException {
        HelloApplication.changeScene("register-page.fxml");
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static boolean checkIfMovieInWatchList(String id){
        boolean contains = false;
        for (Movie m : loggedInUser.getWatchlistMovie()){
            if (m.getId().equals(id)) contains= true;
        }
        return contains;
    }

    public static void updateWatchList(){
        WatchListDAO w = new WatchListDAO();
        List<Movie> l = w.initializeWatchList(loggedInUser.getUsername());
        loggedInUser.updateWatchList(l);
    }

    public static void removeWatchList(String movieid){
        WatchListDAO w = new WatchListDAO();
        w.removeMovieFromUserWatchList(UserController.getLoggedInUser().getUsername(), movieid);
        List<Movie> l = w.initializeWatchList(loggedInUser.getUsername());
        loggedInUser.updateWatchList(l);

    }
    public static void addWatchList(String movieid){
        WatchListDAO w = new WatchListDAO();
        w.removeMovieFromUserWatchList(UserController.getLoggedInUser().getUsername(), movieid);
        List<Movie> l = w.initializeWatchList(loggedInUser.getUsername());
        loggedInUser.updateWatchList(l);
    }



}
