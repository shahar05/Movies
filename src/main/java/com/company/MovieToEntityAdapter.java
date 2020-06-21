package com.company;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
        // Component is automatically created when the application is started
class MovieEntityAdapter implements RepresentationModelAssembler<Movie, EntityModel<Movie>> {

    @Override
    public EntityModel<Movie> toModel(Movie movie) {

        return new EntityModel<>(movie,
                linkTo(methodOn(MovieController.class).getSingleMovie(movie.getId())).withSelfRel(),
                linkTo(methodOn(MovieController.class).getAllMovies()).withRel("orders"));
    }
}