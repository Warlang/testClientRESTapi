package com.example.andreas.teliacarrier;

/**
 * Hold data about movie.
 */

public class Movie {

    // Attributes data
    private String title;
    private String year;
    private String imdbID;

    private String genre;
    private String runTime;
    private String imdbRating;
    private String plot;
    private String actors;

    //{"Response":"False","Error":"Movie not found!"}
    //{"Response":"False","Error":"Too many results."}
    //{"Search":[{"Title":"Hello, My Name Is Doris","Year":"2015","imdbID":"tt3766394","Type":"movie","Poster":"https://images-na.ssl-images-amazon.com/images/M/MV5BMTg0NTM3MTI1MF5BMl5BanBnXkFtZTgwMTAzNTAzNzE@._V1_SX300.jpg"}],"totalResults":"547","Response":"True"}

    // Constructor
    public Movie(String title, String year, String imdbID){
        this.title = title;
        this.year = year;
        this.imdbID = imdbID;
        //this.totalResults = totalResults;
        //this.response = response;
        //this.error = error;
    }

    public Movie(String title, String genre, String runTime, String imdbRating, String plot, String actors){
        this.title = title;
        this.genre = genre;
        this.runTime = runTime;
        this.imdbRating= imdbRating;
        this.plot = plot;
        this.actors = actors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }
}
