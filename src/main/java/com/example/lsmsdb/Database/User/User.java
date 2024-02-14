package com.example.lsmsdb.Database.User;

import com.example.lsmsdb.HelloApplication;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.List;

public class User {

    private static boolean isLoggedIn = false;
    private String username;
    private String fullName;
    private List<Integer> watchlist;

    public User(String newUsername, String newFullName, List<Integer> watchlist){
        this.username = newUsername;
        this.fullName = newFullName;
        this.watchlist = watchlist;
    }

    public List<Integer> getWatchlist() {
        return watchlist;
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

}
