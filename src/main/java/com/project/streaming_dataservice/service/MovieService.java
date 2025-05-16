package com.project.streaming_dataservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.streaming_dataservice.model.Movie;
import com.project.streaming_dataservice.repos.MovieRepository;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Добавить фильм
    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    // Получить все фильмы
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
}
