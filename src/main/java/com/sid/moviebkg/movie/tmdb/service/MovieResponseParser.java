package com.sid.moviebkg.movie.tmdb.service;

import com.sid.moviebkg.common.logging.MBkgLogger;
import com.sid.moviebkg.common.logging.MBkgLoggerFactory;
import com.sid.moviebkg.common.model.movie.Movie;
import com.sid.moviebkg.movie.tmdb.dto.MovieDetailsDto;
import com.sid.moviebkg.movie.tmdb.dto.TmdbResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.sid.moviebkg.movie.util.MovieCmnConstants.N;
import static com.sid.moviebkg.movie.util.MovieCmnConstants.Y;

@Service("tmdbMovieParser")
public class MovieResponseParser {

    private MBkgLogger logger = MBkgLoggerFactory.getLogger(MovieResponseParser.class);

    public List<Movie> fetchMoviesFromTmdbResponse(TmdbResponseDto responseDto) {
        List<MovieDetailsDto> movieDetailsDtos = responseDto.getResults();
        List<Movie> movieList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(movieDetailsDtos)) {
            movieList = movieDetailsDtos.stream()
                    .map(movieDetailsDto -> Movie.builder()
                                .id(movieDetailsDto.getId())
                                .title(movieDetailsDto.getTitle())
                                .originalTitle(movieDetailsDto.getOriginalTitle())
                                .description(getDescription(movieDetailsDto.getOverview()))
                                .posterPath(movieDetailsDto.getPosterPath())
                                .backdropPath(movieDetailsDto.getBackdropPath())
                                .releaseDate(isDateValid(movieDetailsDto.getReleaseDate()) ? LocalDate.parse(movieDetailsDto.getReleaseDate()) : null)
                                .originalLanguage(movieDetailsDto.getOriginalLanguage())
                                .duration(null)
                                .rating(movieDetailsDto.getVoteAverage())
                                .genreIds(getGenreIdsAsString(movieDetailsDto.getGenreIds()))
                                .isAdult(Boolean.TRUE.equals(movieDetailsDto.getAdult()) ? Y : N)
                                .createdDateTime(LocalDateTime.now(Clock.systemUTC()))
                                .build()
                    ).toList();
        }
        return movieList;
    }

    private String getDescription(String overview) {
        String description = overview;
        if (StringUtils.hasText(overview) && overview.length() > 600) {
            logger.info("Description is more than 600 characters. Trimming to 600 characters.");
            description = overview.substring(0, 600);
        }
        return description;
    }

    private String getGenreIdsAsString(List<Integer> genreIds) {
        return !CollectionUtils.isEmpty(genreIds) ? genreIds.stream().map(String::valueOf).collect(Collectors.joining(",")) : "";
    }

    private boolean isDateValid(String dateStr) {
        boolean isDateValid = false;
        try {
            LocalDate.parse(dateStr);
            isDateValid = true;
        } catch (Exception e) {
            logger.warn("Invalid Date:{}", dateStr);
        }
        return isDateValid;
    }
}
