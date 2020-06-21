package com.company;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class MovieController {

    private final MovieRepository movieRepository;
    private final MovieEntityAdapter movieEntityAdapter;
    private final MovieDTOFactory movieDTOFactory;

    public API api = new API();

    public MovieController(MovieRepository movieRepository, MovieEntityAdapter movieEntityAdapter, MovieDTOFactory movieDTOFactory) {
        this.movieRepository = movieRepository;
        this.movieEntityAdapter = movieEntityAdapter;
        this.movieDTOFactory = movieDTOFactory;
    }

    @RequestMapping(value = "/movies")
    ResponseEntity<CollectionModel<EntityModel<Movie>>> getAllMovies() {
        return ResponseEntity.ok(movieEntityAdapter.toCollectionModel(movieRepository.findAll()));
    }

    @RequestMapping(value = "/movies", params = "title")
    ResponseEntity<Movie> getAllMovies(@RequestParam String title) {

        Movie movie = null;
        try {
            movie = api.fetchMovie(Search.TITLE, title);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(movie);
    }
    @GetMapping("/movies/{id}")
    ResponseEntity<EntityModel<Movie>> getSingleMovie(@PathVariable Long id) {

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));
        return ResponseEntity.ok(movieEntityAdapter.toModel(movie));
    }

    @PostMapping(value = "/movies", params = "imdbID")
    // deserializes JSON and Maps the HttpRequest body to POJO
    public ResponseEntity<EntityModel<Movie> >addMovieIMDB(@RequestParam String imdbID) {
        Movie movie = null;
        try {
            movie = api.fetchMovie(Search.ID, imdbID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        movieRepository.save(movie);
        return ResponseEntity.created(linkTo(methodOn(MovieController.class)
                .getSingleMovie(movie.getId())).toUri())
                .body(movieEntityAdapter.toModel(movie));
    }

    @PostMapping("/movies")
    // deserializes JSON and Maps the HttpRequest body to POJO
    public ResponseEntity<EntityModel<Movie>> addMovie(@RequestBody Movie movie) {
        Movie savedMovie = movieRepository.save(movie);
        return ResponseEntity
                .created(linkTo(methodOn(MovieController.class).getSingleMovie(savedMovie.getId())).toUri())
                .body(movieEntityAdapter.toModel(savedMovie));
    }

    @PutMapping("/movies/{id}")
    ResponseEntity<?> updateMovies(@RequestBody Movie newMovie, @PathVariable Long id) throws URISyntaxException {
        Movie mov = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));

        if (newMovie.getDirector() != null) mov.setDirector(newMovie.getDirector());
        if (newMovie.getGenre() != null) mov.setGenre(newMovie.getGenre());
        if (newMovie.getPlot() != null) mov.setPlot(newMovie.getPlot());
        if (newMovie.getRating() != null) mov.setRating(newMovie.getRating());
        if (newMovie.getYear() != null) mov.setYear(newMovie.getYear());
        if (newMovie.getPrice() != null) mov.setPrice(newMovie.getPrice());
        if (newMovie.getTitle() != null) mov.setTitle(newMovie.getTitle());

        movieRepository.save(mov);

        return ResponseEntity
                .created(linkTo(methodOn(MovieController.class).getSingleMovie(mov.getId())).toUri())
                .body(movieEntityAdapter.toModel(mov));
    }

    @DeleteMapping("/movies/{id}")
    ResponseEntity<?> deleteMovie(@PathVariable long id) {
        movieRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @RequestMapping(value = "/moviesDTO", params = "title")
    ResponseEntity<EntityModel<MovieDTO>> getAllMoviesDTOS(@RequestParam String title) {
        Movie movie = movieRepository.findByTitle(title).orElseThrow(() -> new MovieNotFoundException(title));
        MovieDTO movieDTO = new MovieDTO(movie);
        return ResponseEntity.ok(movieDTOFactory.toModel(movieDTO));
    }


    @GetMapping("/movies/{id}/singleDTO")
    public ResponseEntity<EntityModel<MovieDTO>>
    formattedMovieDTO(@PathVariable Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));
        MovieDTO movieDTO = new MovieDTO(movie);
        return ResponseEntity.ok(movieDTOFactory.toModel(movieDTO));
    }

    @GetMapping("/movies/allDTO")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<CollectionModel<EntityModel<MovieDTO>>> getAllMoviesDTO() {
        return ResponseEntity.ok(movieDTOFactory.toCollectionModel(convertMoviesToDTO(movieRepository.findAll())));
    }

    ArrayList<MovieDTO> convertMoviesToDTO(List<Movie> movies) {
        ArrayList<MovieDTO> movieDTOS = new ArrayList<>();
        for (Movie movie : movies) {
            movieDTOS.add(new MovieDTO(movie));
        }
        return movieDTOS;
    }
}
