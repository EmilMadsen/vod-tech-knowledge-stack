package com.example.projectvodtech.controller;

import com.example.projectvodtech.exception.ResourceNotFoundException;
import com.example.projectvodtech.model.Movie;
import com.example.projectvodtech.repository.MovieRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@RequestMapping(path="/api")
public class MovieController {

    MeterRegistry registry;

    private Counter counter = Metrics.counter("movie.calls", "uri", "/messages");

    private Iterable<Movie> movieList;

    private ArrayList<String> test = new ArrayList<>();

    @Autowired
    private MovieRepository movieRepository;

    public MovieController(MeterRegistry registry) {
        test.add("test");
        test.add("2");
        test.add("3");
        test.add("4");
        registry.gaugeCollectionSize("movie.size", Tags.empty(), (ArrayList) movieList);
    }

    //    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/movie")
    public @ResponseBody Iterable<Movie> getAllMovies() {

        counter.increment();
        movieList = movieRepository.findAll();
        test.add("3");

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