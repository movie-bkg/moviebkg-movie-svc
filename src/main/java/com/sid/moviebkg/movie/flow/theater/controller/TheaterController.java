package com.sid.moviebkg.movie.flow.theater.controller;

import com.sid.moviebkg.common.dto.ResponseMsgDto;
import com.sid.moviebkg.common.logging.MBkgLogger;
import com.sid.moviebkg.common.logging.MBkgLoggerFactory;
import com.sid.moviebkg.common.utils.ExceptionUtils;
import com.sid.moviebkg.movie.flow.exception.MovieFlowException;
import com.sid.moviebkg.movie.flow.movie.controller.MovieController;
import com.sid.moviebkg.movie.flow.movie.dto.request.MovieRequestDto;
import com.sid.moviebkg.movie.flow.theater.dto.request.TheaterRequestDto;
import com.sid.moviebkg.movie.flow.theater.service.TheaterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.sid.moviebkg.movie.util.MovieCmnConstants.CONTENT_TYPE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/theater")
@SecurityRequirement(name = "Authorization")
public class TheaterController {
    private MBkgLogger logger = MBkgLoggerFactory.getLogger(MovieController.class);
    private final ExceptionUtils exceptionUtils;
    private final TheaterService theaterService;

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> saveTheaterDetails(@RequestBody TheaterRequestDto requestDto) {
        try {
            theaterService.saveTheater(requestDto);
        } catch (Exception exception) {
            throw exceptionUtils.getException(exception, ex -> new MovieFlowException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()),
                    MovieFlowException.class::isInstance);
        }
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Saved successfully\"}");
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
