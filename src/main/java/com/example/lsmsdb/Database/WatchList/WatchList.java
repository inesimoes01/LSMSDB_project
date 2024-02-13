package com.example.lsmsdb.Database.WatchList;

import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.User.User;
import com.example.lsmsdb.HelloApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WatchList {

    public static void addMovieToWatchList(int id){
        WatchListDAO.addMovieToUserWatchList(id);
    }

    public static void removeMovie(int id) throws IOException {
        WatchListDAO.removeMovieFromUserWatchList(id);
        HelloApplication.changeScene("profile-page.fxml");
    }
}
