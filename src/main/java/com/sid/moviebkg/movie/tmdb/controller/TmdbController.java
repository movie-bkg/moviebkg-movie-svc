package com.sid.moviebkg.movie.tmdb.controller;

import com.sid.moviebkg.common.dto.MessageDto;
import com.sid.moviebkg.common.dto.ResponseMsgDto;
import com.sid.moviebkg.common.logging.MBkgLogger;
import com.sid.moviebkg.common.logging.MBkgLoggerFactory;
import com.sid.moviebkg.common.utils.ExceptionUtils;
import com.sid.moviebkg.movie.flow.exception.MovieFlowException;
import com.sid.moviebkg.movie.tmdb.service.FetchTmdbMoviesService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sid.moviebkg.movie.util.MovieCmnConstants.CONTENT_TYPE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tmdb")
@SecurityRequirement(name = "Authorization")
public class TmdbController {

    private MBkgLogger logger = MBkgLoggerFactory.getLogger(TmdbController.class);
    private final ExceptionUtils exceptionUtils;
    private final FetchTmdbMoviesService fetchTmdbMoviesService;

    @GetMapping(value = "/genres", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> fetchTmdbGenres() {
        int response = 0;
        try {
            response = fetchTmdbMoviesService.fetchAndSaveGenres();
        } catch (Exception exception) {
            throw exceptionUtils.getException(exception, ex -> new MovieFlowException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()),
                    MovieFlowException.class::isInstance);
        }
        String message = "Persisted " + response + " genres";
        MessageDto messageDto = MessageDto.builder()
                .code("200")
                .message(message)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(messageDto);
    }

    @GetMapping(value = "/movies", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> fetchTmdbMovies() {
        int response = 0;
        try {
            response = fetchTmdbMoviesService.fetchAndSaveMoviesFromTmdb();
        } catch (Exception exception) {
            throw exceptionUtils.getException(exception, ex -> new MovieFlowException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()),
                    MovieFlowException.class::isInstance);
        }
        String message = "Persisted " + response + " movies";
        MessageDto messageDto = MessageDto.builder()
                .code("200")
                .message(message)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(messageDto);
    }

    @ExceptionHandler(value = {MovieFlowException.class})
    public ResponseEntity<Object> handleFailure(MovieFlowException ex) {
        ResponseMsgDto responseMsgDto = ex.getResponseMsgDto();
        logger.warn("Failed with Error:{}", ex.getError());
        return ResponseEntity.status(ex.getStatus().value())
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(responseMsgDto);
    }
}
