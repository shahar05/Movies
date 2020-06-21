package com.company;

public class MovieJson {
    private String Title;
    private Integer Year;
    private String Director;
    private String imdbID;
    private String Genre;
    private Double imdbRating;
    private String Plot;

    public MovieJson(String title, Integer year, String director, String imdbID, String genre, Double imdbRating, String plot) {
        Title = title;
        Year = year;
        Director = director;
        this.imdbID = imdbID;
        Genre = genre;
        this.imdbRating = imdbRating;
        Plot = plot;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Integer getYear() {
        return Year;
    }

    public void setYear(Integer year) {
        Year = year;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public Double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(Double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }
}
