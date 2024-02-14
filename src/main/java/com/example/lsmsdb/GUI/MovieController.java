package com.example.lsmsdb.GUI;

import com.example.lsmsdb.Database.Movie.Movie;

public class MovieController {
    private static Movie currentMovie;

    public static Movie getCurrentMovie() {
        return currentMovie;
    }

    public static void setCurrentMovie(Movie currentMovie) {
        MovieController.currentMovie = currentMovie;
    }



}
