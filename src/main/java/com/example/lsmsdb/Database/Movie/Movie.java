package com.example.lsmsdb.Database.Movie;

public class Movie {

    private int id;

    private String title;
    private int year;

    private String genre;
    private String poster;

    private double rating;


    public Movie(int id, String title, int year, String poster, double rating, String genre){
        this.id = id;
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.poster = poster;
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
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


}
