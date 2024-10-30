package com.sid.moviebkg.movie.tmdb.service;

public interface FetchTmdbMoviesService {
    int fetchAndSaveMoviesFromTmdb();
    int fetchAndSaveGenres();
}
