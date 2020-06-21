package com.company;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data // For Lombok auto-generated methods
@Entity // For JPA-based data store.
@Table(name = "movies")
//implements java.io.Serializable
public class Movie {
    private @Id
    @GeneratedValue
    Long id;
    private String title;
    private Integer year;
    private String director;
    private Double rating;
    private String imdbID;
    private String genre;
    private String plot;
    private Double price;
    @ManyToMany(mappedBy = "movieList")
    @JsonBackReference
    Set<Order> orderList = new HashSet<>();

    public Movie() {
    }

    public Movie(String title, Integer year, String director) {
        this.title = title;
        this.year = year;
        this.director = director;
    }

    public Movie(String title, Integer year, String director, Double rating, String genre, String plot, Double price) {
        this.title = title;
        this.year = year;
        this.director = director;
        this.rating = rating;
        this.genre = genre;
        this.plot = plot;
        this.price = price;
    }

    public Movie(String title, Integer year, String director, Double rating, String imdbID, String genre, String plot) {
        this.title = title;
        this.year = year;
        this.director = director;
        this.rating = rating;
        this.imdbID = imdbID;
        this.genre = genre;
        this.plot = plot;
    }


    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setOrderList(Set<Order> orderList) {
        this.orderList = orderList;
    }

    public Set<Order> getOrderList() {
        return orderList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }


        @Override
    public String toString() {
        return "title   " + title
                + "year   "+  year
                + "director  "+ director
                + "rating   "+ rating
                + "imdbID  " + imdbID
                + "genre  "+ genre
                + "plot  " + plot
                + "price  " + price;
    }
}
