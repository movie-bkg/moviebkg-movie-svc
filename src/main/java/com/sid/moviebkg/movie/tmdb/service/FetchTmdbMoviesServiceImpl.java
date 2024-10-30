package com.sid.moviebkg.movie.tmdb.service;

import com.sid.moviebkg.common.logging.MBkgLogger;
import com.sid.moviebkg.common.logging.MBkgLoggerFactory;
import com.sid.moviebkg.common.model.genre.Genre;
import com.sid.moviebkg.common.model.movie.Movie;
import com.sid.moviebkg.movie.flow.movie.repository.MovieRepository;
import com.sid.moviebkg.movie.tmdb.dto.GenreResponseDto;
import com.sid.moviebkg.movie.tmdb.dto.TmdbResponseDto;
import com.sid.moviebkg.movie.tmdb.repository.GenreRepository;
import com.sid.moviebkg.movie.tmdb.util.TmdbCmnMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FetchTmdbMoviesServiceImpl implements FetchTmdbMoviesService {

    private MBkgLogger logger = MBkgLoggerFactory.getLogger(FetchTmdbMoviesServiceImpl.class);
    @Value("${tmdb.movies.endpoint}")
    private String tmdbMoviesEndPoint;
    @Value("${tmdb.genres.endpoint}")
    private String tmdbGenresEndPoint;
    private final RestTemplate restTemplate;
    private final GenreRepository genreRepository;
    private final TmdbCmnMapper tmdbCmnMapper;
    private final MovieResponseParser movieResponseParser;
    private final MovieRepository movieRepository;

    public FetchTmdbMoviesServiceImpl(@Qualifier("tmdbRestTemplate") RestTemplate restTemplate, GenreRepository genreRepository,
                                      TmdbCmnMapper tmdbCmnMapper, @Qualifier("tmdbMovieParser") MovieResponseParser movieResponseParser, MovieRepository movieRepository) {
        this.restTemplate = restTemplate;
        this.genreRepository = genreRepository;
        this.tmdbCmnMapper = tmdbCmnMapper;
        this.movieResponseParser = movieResponseParser;
        this.movieRepository = movieRepository;
    }

    @Transactional
    @Override
    public int fetchAndSaveMoviesFromTmdb() {
        int total = 0;
        long start = System.currentTimeMillis();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<TmdbResponseDto> response = restTemplate.exchange(tmdbMoviesEndPoint, HttpMethod.GET, entity, TmdbResponseDto.class);
        long end = System.currentTimeMillis();
        long timeTaken = end - start;
        logger.info("Time taken to fetch movies from TMDB is:{} ms", timeTaken);
        if (response.getStatusCode().value() == HttpStatus.OK.value() || response.getStatusCode().value() == HttpStatus.ACCEPTED.value()) {
            logger.info("Response from TMDB:{}", response);
            TmdbResponseDto tmdbResponseDto = response.getBody();
            List<Movie> movies = movieResponseParser.fetchMoviesFromTmdbResponse(tmdbResponseDto);
            for (Movie movie : movies) {
                try {
                    saveMovie(movie);
                    total += 1;
                } catch (Exception e) {
                    logger.warn("Exception occurred while persisting Movie with Title:{}", movie.getTitle());
                }
            }
        }
        logger.info("Persisted {} rows to T_Movie", total);
        return total;
    }

    @Transactional
    public void saveMovie(Movie movie) {
        movieRepository.save(movie);
    }

    @Transactional
    @Override
    public int fetchAndSaveGenres() {
        int total = 0;
        long start = System.currentTimeMillis();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<GenreResponseDto> response = restTemplate.exchange(tmdbGenresEndPoint, HttpMethod.GET, entity, GenreResponseDto.class);
        long end = System.currentTimeMillis();
        long timeTaken = end - start;
        logger.info("Time taken to fetch genres from TMDB is:{} ms", timeTaken);
        if (response.getStatusCode().value() == HttpStatus.OK.value() || response.getStatusCode().value() == HttpStatus.ACCEPTED.value()) {
            logger.info("Response from TMDB:{}", response);
            GenreResponseDto genreResponseDto = response.getBody();
            total = saveGenres(genreResponseDto);
        }
        return total;
    }

    private int saveGenres(GenreResponseDto response) {
        int total = 0;
        try {
            if (!CollectionUtils.isEmpty(response.getGenres())) {
                List<GenreResponseDto.GenreDto> genresDtos = response.getGenres();
                List<Genre> genres = tmdbCmnMapper.mapGenreDtosToGenre(genresDtos);
                List<Genre> genresPersisted = genreRepository.saveAll(genres);
                total = genresPersisted.size();
                logger.info("Persisted {} rows to T_Genre", total);
            }
        } catch (Exception e) {
            logger.warn("Exception occurred during persisting Genres");
        }
        return total;
    }
}
