package com.example.lsmsdb.Database.User;

import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.WatchList.WatchListDAO;
import com.example.lsmsdb.HelloApplication;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class User {

    private static boolean isLoggedIn = false;
    private String username;
    private String fullName;
    //private List<String> watchlist;
    private String profileImage;

    private List<Movie> watchlistMovie;

    public User(String newUsername, String newFullName, String profileImage, List<Movie> watchlistMovie){
        this.username = newUsername;
        this.fullName = newFullName;
        //this.watchlist = watchlist;
        this.profileImage = profileImage;
        this.watchlistMovie = watchlistMovie;
    }

    public List<Movie> getWatchlistMovie() {
        return watchlistMovie;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        User.isLoggedIn = loggedIn;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileImage(){
        if (profileImage == null){
            return "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png";
        }else {
            return "https://image.tmdb.org/t/p/w500" + profileImage;
        }
    }

    public static String getDefaultProfileImage(){
        return "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png";
    }

    public void updateWatchList(List<Movie> newWL){
        //watchlistMovie = new ArrayList<>();
        watchlistMovie = newWL;
    }

}
