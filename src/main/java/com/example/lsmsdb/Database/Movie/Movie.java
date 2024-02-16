package com.example.lsmsdb.Database.Movie;

import java.util.List;

public class Movie {

    private String id;
    private String title;
    private int year;
    private List<String> genre;
    private String poster;
    private double rating;
    private int reviewNumber;

    private String overview;



    private Double popularity;


    public Movie(String id, String title, String poster){
        this.id = id;
        this.title = title;
        this.poster = poster;
    }

    public Movie(String id, String title, int year, String poster, double rating, List<String> genre, int reviewNumber, String overview, Double popularity){
        this.id = id;
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.poster = poster;
        this.rating = rating;
        this.reviewNumber = reviewNumber;
        this.overview = overview;
        this.popularity = popularity;
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

    public String getPosterURL() {
        return "https://image.tmdb.org/t/p/w500" + poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }


    public String getId() {
        return id;
    }
    public int getReviewNumber() {
        return reviewNumber;
    }

    public Double getPopularity() {
        return popularity;
    }
}
