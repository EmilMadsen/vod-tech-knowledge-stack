package com.example.projectvodtech.controller;

import com.example.projectvodtech.exception.ResourceNotFoundException;
import com.example.projectvodtech.model.Movie;
import com.example.projectvodtech.repository.MovieRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(path="/api")
public class MovieController {

    private Counter counter = Metrics.counter("movie.calls", "uri", "/messages");

    private Iterable<Movie> movieList;

    @Autowired
    private MovieRepository movieRepository;

    //    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/movie")
    public @ResponseBody Iterable<Movie> getAllMovies() {

        counter.increment();
        movieList = movieRepository.findAll();

        return movieList;
    }

    @PostMapping("/movie")
    public @ResponseBody Movie createNote(@Valid @RequestBody Movie movie) {

        return movieRepository.save(movie);
    }

    @GetMapping("/movie/{id}")
    public @ResponseBody Movie getMovieById(@PathVariable(value = "id") Integer movieId) {

        return movieRepository.findById(movieId).orElseThrow(() -> new ResourceNotFoundException("Movie", "id", movieId));
    }

    @PutMapping("/movie/{id}")
    public @ResponseBody Movie updateMovie (@PathVariable(value = "id") Integer movieId, @Valid @RequestBody Movie movieDetails) {

        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new ResourceNotFoundException("Movie", "Id", movieId));

        movie.setTitle(movieDetails.getTitle());
        movie.setCreator(movieDetails.getCreator());
        movie.setProductionYear(movieDetails.getProductionYear());

        return movieRepository.save(movie);
    }

    @DeleteMapping("movie/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable(value = "id") Integer movieId) {

        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new ResourceNotFoundException("Movie", "Id", movieId));

        movieRepository.delete(movie);

        return ResponseEntity.ok().build();
    }
}