package com.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

@Value
@JsonPropertyOrder({"title", "year", "director", "rating", "genre", "plot"})
public class MovieDTO {
    @JsonIgnore
    private final Movie movie;

    public MovieDTO(Movie movie) {
        this.movie = movie;
    }

    @JsonIgnore
    public Long getId() {
        return this.movie.getId();
    }

    public String getTitle() {
        return this.movie.getTitle();
    }

    public int getYear() {
        return this.movie.getYear();
    }

    public String getDirector() {
        return this.movie.getDirector();
    }

    public Double getRating() {
        return this.movie.getRating();
    }

    public String getGenre() {
        return this.movie.getGenre();
    }

    public String getPlot() {
        return this.movie.getPlot();
    }


}
