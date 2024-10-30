package com.sid.moviebkg.movie.flow.movie.service;

import com.sid.moviebkg.common.model.genre.Genre;
import com.sid.moviebkg.common.model.movie.Movie;
import com.sid.moviebkg.movie.ui.dtos.MovieResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.sid.moviebkg.movie.util.MovieCmnConstants.Y;

@Primary
@Service
@RequiredArgsConstructor
public class MovieResponseParser {

    private final ModelMapper modelMapper;

    public MovieResponseDto handleMovieResponse(Movie movie, List<Genre> genres) {
        MovieResponseDto responseDto = MovieResponseDto.builder().build();
        if (movie != null && !CollectionUtils.isEmpty(genres)) {
            responseDto = modelMapper.map(movie, MovieResponseDto.class);
            List<MovieResponseDto.GenreResponseDto> genreDtos = genres.stream().map(genre -> modelMapper.map(genre, MovieResponseDto.GenreResponseDto.class)).toList();
            responseDto.setGenres(genreDtos);
            responseDto.setAdult(StringUtils.hasText(movie.getIsAdult()) && movie.getIsAdult().equals(Y));
            responseDto.setReleaseDate(movie.getReleaseDate().toString());
        }
        return responseDto;
    }
}
