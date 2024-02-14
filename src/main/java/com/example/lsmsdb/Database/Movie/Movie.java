package com.example.lsmsdb.Database.Movie;

import java.util.List;

public class Movie {

    private int id;
    private String title;
    private int year;
    private List<String> genre;
    private String poster;
    private double rating;
    private int reviewNumber;

    private String overview;


    public Movie(int id, String title, int year, String poster, double rating, List<String> genre, int reviewNumber){
        this.id = id;
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.poster = poster;
        this.rating = rating;
        this.reviewNumber = reviewNumber;
    }

    public Movie(int id, String title, int year, String poster, double rating, List<String> genre, int reviewNumber, String overview){
        this.id = id;
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.poster = poster;
        this.rating = rating;
        this.reviewNumber = reviewNumber;
        this.overview = overview;
    }

    public String getGenre() {
        String result = "";
        for (String m : this.genre){
            result += m + ", ";
        }
        return result;
    }
    public String getOverview() {
        return overview;
    }
    public double getRating() {
        return rating;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }


    public int getId() {
        return id;
    }
    public int getReviewNumber() {
        return reviewNumber;
    }


}
