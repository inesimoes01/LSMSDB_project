package com.example.lsmsdb.Database.Review;

import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.User.User;
import com.example.lsmsdb.Database.User.UserDAO;
import javafx.scene.image.Image;

public class Review {
    private User reviewUser;
    private String username;
    private String reviewText;
    private String movieid;
    private Double rating;
    private String timestamp;
    private String profilepic;

    public Review(String username, String reviewText, String movieid, Double rating, String timestamp, String profilepic) {
        this.username = username;
        this.reviewText = reviewText;
        this.movieid = movieid;
        this.rating = rating;
        this.timestamp = timestamp;
        this.profilepic = profilepic;
    }

    public User getReviewUser(String username) {

        return UserDAO.getUserFromUsername(username);
    }

    public String getReviewText() {
        return reviewText;
    }

    public String getReviewMovie() {
        return movieid;
    }

    public Double getRating() {
        return rating;
    }

    public String getTimestamp() {
        return timestamp;
    }


    public String getUsername() {
        return username;
    }

    public String getProfilepic(){
        if (profilepic == null){
            return "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png";
        }else {
            return "https://image.tmdb.org/t/p/w500" + profilepic;
        }
    }



}
