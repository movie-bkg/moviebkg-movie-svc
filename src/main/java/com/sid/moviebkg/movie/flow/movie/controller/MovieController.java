package com.sid.moviebkg.movie.flow.movie.controller;

import com.sid.moviebkg.common.dto.ResponseMsgDto;
import com.sid.moviebkg.common.logging.MBkgLogger;
import com.sid.moviebkg.common.logging.MBkgLoggerFactory;
import com.sid.moviebkg.common.utils.ExceptionUtils;
import com.sid.moviebkg.movie.flow.exception.MovieFlowException;
import com.sid.moviebkg.movie.flow.movie.dto.request.MovieRequestDto;
import com.sid.moviebkg.movie.flow.movie.service.MovieService;
import com.sid.moviebkg.movie.ui.dtos.MovieResponseDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import static com.sid.moviebkg.movie.util.MovieCmnConstants.CONTENT_TYPE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
@SecurityRequirement(name = "Authorization")
public class MovieController {

    private MBkgLogger logger = MBkgLoggerFactory.getLogger(MovieController.class);
    private final ExceptionUtils exceptionUtils;
    private final MovieService movieService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> saveMovieDetails(@RequestBody MovieRequestDto requestDto) {
        try {
            movieService.saveMovies(requestDto);
        } catch (Exception exception) {
            throw exceptionUtils.getException(exception, ex -> new MovieFlowException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()),
                    MovieFlowException.class::isInstance);
        }
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Saved successfully\"}");
    }

    @GetMapping(value = "/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovieResponseDto> findMovieById(@PathVariable String movieId) {
        MovieResponseDto response;
        try {
            if (!StringUtils.hasText(movieId)) {
                ResponseMsgDto responseMsgDto = ResponseMsgDto.builder()
                        .exception("Invalid request. MovieId not present.")
                        .build();
                throw new MovieFlowException(HttpStatus.BAD_REQUEST, responseMsgDto);
            }
            response = movieService.findMovieById(movieId);
        } catch (Exception exception) {
            throw exceptionUtils.getException(exception, ex -> new MovieFlowException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()),
                    MovieFlowException.class::isInstance);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
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
