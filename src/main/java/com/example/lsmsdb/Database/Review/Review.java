package com.example.lsmsdb.Database.Review;

import com.example.lsmsdb.Database.Movie.Movie;
import com.example.lsmsdb.Database.User.User;

public class Review {
    private User reviewUser;
    private String reviewText;
    private Movie reviewMovie;
    private Double rating;

    private String timestamp;

    public Review(User reviewUser, String reviewText, Movie reviewMovie, Double rating, String timestamp) {
        this.reviewUser = reviewUser;
        this.reviewText = reviewText;
        this.reviewMovie = reviewMovie;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    public User getReviewUser() {
        return reviewUser;
    }

    public String getReviewText() {
        return reviewText;
    }

    public Movie getReviewMovie() {
        return reviewMovie;
    }

    public Double getRating() {
        return rating;
    }

    public String getTimestamp() {
        return timestamp;
    }


}
