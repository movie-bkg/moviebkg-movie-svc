package com.sid.moviebkg.movie.tmdb.util;

import com.sid.moviebkg.common.model.genre.Genre;
import com.sid.moviebkg.movie.tmdb.dto.GenreResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TmdbCmnMapper {
    public List<Genre> mapGenreDtosToGenre(List<GenreResponseDto.GenreDto> genreDtos) {
        return genreDtos.stream()
                .map(genreDto -> Genre.builder()
                        .genreId(genreDto.getId())
                        .genreName(genreDto.getName())
                        .build()).toList();
    }
}
