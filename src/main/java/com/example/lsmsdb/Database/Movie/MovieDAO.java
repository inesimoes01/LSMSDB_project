package com.example.lsmsdb.Database.Movie;

public class MovieDAO {

    //TODO change to id after and accessing the database
    public Movie getMovieFromName(String name){
        Movie m = new Movie("Parasite", 1992, "https://m.media-amazon.com/images/I/81hsswTfQXL._AC_UF894,1000_QL80_.jpg");
        return m;
    }
}
