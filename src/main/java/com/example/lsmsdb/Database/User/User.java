package com.example.lsmsdb.Database.User;

import com.example.lsmsdb.HelloApplication;
import javafx.scene.image.Image;

import java.io.IOException;

public class User {

    private static boolean isLoggedIn = false;
    private static String username;
    private static String fullName;
    private static Image profilePic;

    public static void setUser(String newUsername, String newFullName){
        username = newUsername;
        fullName = newFullName;
    }

    public static boolean isIsLoggedIn() {
        return isLoggedIn;
    }


    public static String getFullName() {
        return fullName;
    }


    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static void setLoggedIn(boolean loggedIn) {
        User.isLoggedIn = loggedIn;
    }

    public static String getUsername() {
        return username;
    }
    public static Image getProfilePic() {
        return profilePic;
    }


}
