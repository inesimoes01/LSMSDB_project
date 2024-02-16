package com.example.lsmsdb.Database.WatchList;

import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.User.User;
import com.example.lsmsdb.GUI.UserController;
import com.example.lsmsdb.HelloApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WatchList {

    public static void addMovieToWatchList(String id, String title, String poster){
        WatchListDAO w = new WatchListDAO();
        w.addMovieToUserWatchList(UserController.getLoggedInUser().getUsername(), id, title, poster);
    }

    public static void removeMovieProfile() throws IOException {
        HelloApplication.changeScene("profile-page.fxml");
    }
    public static void removeMovieMain(String id) throws IOException {
        WatchListDAO w = new WatchListDAO();
        w.removeMovieFromUserWatchList(UserController.getLoggedInUser().getUsername(), id);
    }
}
