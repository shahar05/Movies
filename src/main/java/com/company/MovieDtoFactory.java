package com.company;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component // Component is automatically created when the application is started
class MovieDTOFactory implements RepresentationModelAssembler<MovieDTO, EntityModel<MovieDTO>> {

    @Override
    public EntityModel<MovieDTO> toModel(MovieDTO movieDTO) {

        return new EntityModel<>(movieDTO,
                linkTo(methodOn(MovieController.class).formattedMovieDTO(movieDTO.getId())).withSelfRel(),
                linkTo(methodOn(MovieController.class).getAllMoviesDTO()).withRel("movies"));
    }
}