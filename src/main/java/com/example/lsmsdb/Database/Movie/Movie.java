package com.example.lsmsdb.Database.Movie;

public class Movie {

    private String title;
    private int year;
    private String poster;

    public Movie(String title, int year, String poster){
        this.title = title;
        this.year = year;
        this.poster = poster;
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




}
