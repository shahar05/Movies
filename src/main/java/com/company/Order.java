package com.company;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Data
@Entity
@Table(name = "orders")

public class Order {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDate creationDate = LocalDate.now();
    private LocalDate purchaseDate;
    private String title;
    private Double price = 0.0;

    @ManyToMany
    @JoinTable(
            name = "movies_ordered",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    // @JsonManagedReference
    private Set<Movie> movieList = new HashSet<>();


    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @JsonIgnore
    Customer customer;

    public Order() {
    }

    public Order(String title) {
        this.title = title;
        this.creationDate = LocalDate.now();
    }
    public void removeMovieFromList(Movie movie){
        if (movie == null) return;
        price -= movie.getPrice();
        this.getMovieList().remove(movie);
    }
    public void addMovieToList(Movie movie){
        if (movie == null) return;
        price += movie.getPrice();
        this.getMovieList().add(movie);
    }


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        if (this.customer != null)return;
        this.purchaseDate = LocalDate.now();
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Set<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(Set<Movie> movieList) {
        this.movieList = movieList;
        price = 0.0;
        movieList.forEach((mov)-> price += mov.getPrice()    );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, title, price);
    }

    @Override
    public String toString() {
        return "id  " + id + " getTitle  " + getTitle() ;
    }
}
