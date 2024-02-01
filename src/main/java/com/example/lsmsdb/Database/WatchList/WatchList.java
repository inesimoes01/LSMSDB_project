package com.example.lsmsdb.Database.WatchList;

import com.example.lsmsdb.Database.Movie.Movie;

import java.util.ArrayList;
import java.util.List;

public class WatchList {
    private static List<Movie> movieWatchList;

    public static List<Movie> getWatchList(){
        List<Movie> movieList = new ArrayList<>();
        Movie m1 = new Movie("Parasite", 2019, "https://m.media-amazon.com/images/I/81hsswTfQXL._AC_UF894,1000_QL80_.jpg");
        Movie m2 = new Movie("Nemo", 2009, "https://m.media-amazon.com/images/I/81hsswTfQXL._AC_UF894,1000_QL80_.jpg");
        Movie m4 = new Movie("Fight Club", 2001, "https://m.media-amazon.com/images/I/81hsswTfQXL._AC_UF894,1000_QL80_.jpg");
        Movie m6 = new Movie("500 Days of Summer", 2016, "https://m.media-amazon.com/images/I/81hsswTfQXL._AC_UF894,1000_QL80_.jpg");


        movieList.add(m1);
        movieList.add(m2);
        movieList.add(m4);
        movieList.add(m6);

        return movieList;
    }

    public static boolean addMovieToWatchList(Movie movie){
        return movieWatchList.add(movie);
    }
}
