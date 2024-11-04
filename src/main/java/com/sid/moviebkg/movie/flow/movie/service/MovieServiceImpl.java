package com.sid.moviebkg.movie.flow.movie.service;

import com.sid.moviebkg.common.dto.ResponseMsgDto;
import com.sid.moviebkg.common.logging.MBkgLogger;
import com.sid.moviebkg.common.logging.MBkgLoggerFactory;
import com.sid.moviebkg.common.model.genre.Genre;
import com.sid.moviebkg.common.model.movie.Movie;
import com.sid.moviebkg.common.utils.ValidationUtil;
import com.sid.moviebkg.movie.flow.exception.MovieFlowException;
import com.sid.moviebkg.movie.flow.movie.dto.request.MovieDto;
import com.sid.moviebkg.movie.flow.movie.dto.request.MovieRequestDto;
import com.sid.moviebkg.movie.flow.movie.repository.MovieRepository;
import com.sid.moviebkg.movie.flow.user.service.FetchUserService;
import com.sid.moviebkg.movie.mapper.CmnMapper;
import com.sid.moviebkg.movie.tmdb.repository.GenreRepository;
import com.sid.moviebkg.movie.ui.dtos.MovieResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private MBkgLogger logger = MBkgLoggerFactory.getLogger(MovieServiceImpl.class);
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ValidationUtil validationUtil;
    private final CmnMapper cmnMapper;
    private final MovieResponseParser movieResponseParser;
    private final FetchUserService fetchUserService;

    @Transactional
    @Override
    public void saveMovies(MovieRequestDto movieRequestDto) {
        if (!isRequestValid(movieRequestDto.getMovies())) {
            ResponseMsgDto responseMsgDto = ResponseMsgDto.builder()
                    .exception("Mandatory fields are not present. All fields are mandatory.")
                    .build();
            throw new MovieFlowException(HttpStatus.BAD_REQUEST, responseMsgDto);
        }
        List<Movie> movies = cmnMapper.mapMovieDtoToMovie(movieRequestDto.getMovies());
        movieRepository.saveAll(movies);
    }

    @Override
    public MovieResponseDto findMovieById(String movieId) {
        Optional<Movie> movieOptional = movieRepository.findById(movieId);
        if (movieOptional.isEmpty()) {
            ResponseMsgDto responseMsgDto = ResponseMsgDto.builder()
                    .exception("Movie not found. Please check the Id.")
                    .build();
            throw new MovieFlowException(HttpStatus.NOT_FOUND, responseMsgDto);
        }
        Movie movie;
        List<Genre> genres;
        try {
            movie = movieOptional.get();
            List<Integer> genreIds = new ArrayList<>();
            if (StringUtils.hasText(movie.getGenreIds())) {
                genreIds = getGenreIds(movie.getGenreIds());
            }
            genres = new ArrayList<>();
            if (!CollectionUtils.isEmpty(genreIds)) {
                genres = genreRepository.findAllById(genreIds);
            }
        } catch (Exception e) {
            ResponseMsgDto responseMsgDto = ResponseMsgDto.builder()
                    .exception("Internal Server Error")
                    .build();
            throw new MovieFlowException(HttpStatus.INTERNAL_SERVER_ERROR, responseMsgDto);
        }
        return movieResponseParser.handleMovieResponse(movie, genres);
    }

    private List<Integer> getGenreIds(String genreIds) {
        return Arrays.stream(genreIds.split(",")).map(Integer::parseInt).toList();
    }

    private boolean isRequestValid(List<MovieDto> movies) {
        return !CollectionUtils.isEmpty(movies) &&
                validationUtil.validateList(movies, movieDto ->
                        !StringUtils.hasText(movieDto.getTitle()) ||
                                !StringUtils.hasText(movieDto.getDescription()) ||
                                !StringUtils.hasText(movieDto.getDirector()) ||
                                !StringUtils.hasText(movieDto.getProducer()) ||
                                !isDateValid(movieDto.getReleaseDate()) ||
                                !StringUtils.hasText(movieDto.getLanguage()) ||
                                !StringUtils.hasText(movieDto.getGenre()) ||
                                movieDto.getDuration() < 0 ||
                                movieDto.getRating() < 0
                );
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
