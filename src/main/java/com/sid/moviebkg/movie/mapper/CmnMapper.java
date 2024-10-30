package com.sid.moviebkg.movie.mapper;

import com.sid.moviebkg.common.model.movie.Movie;
import com.sid.moviebkg.movie.flow.movie.dto.request.MovieDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CmnMapper {
    private final ModelMapper modelMapper;

    public CmnMapper(@Qualifier("movieModelMapper") ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<Movie> mapMovieDtoToMovie(List<MovieDto> movieDtos) {
        return movieDtos.stream()
                .map(movieDto -> {
                            Movie movie = modelMapper.map(movieDto, Movie.class);
                            LocalDateTime datetime = LocalDateTime.now();
                            movie.setReleaseDate(LocalDate.parse(movieDto.getReleaseDate()));
                            movie.setCreatedDateTime(datetime);
                            return movie;
                        }
                ).toList();
    }

}
