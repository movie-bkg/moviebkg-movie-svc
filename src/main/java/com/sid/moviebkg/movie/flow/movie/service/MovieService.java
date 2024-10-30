package com.sid.moviebkg.movie.flow.movie.service;

import com.sid.moviebkg.movie.flow.movie.dto.request.MovieRequestDto;
import com.sid.moviebkg.movie.ui.dtos.MovieResponseDto;

public interface MovieService {
    void saveMovies(MovieRequestDto movieRequestDto);

    MovieResponseDto findMovieById(String movieId);
}
