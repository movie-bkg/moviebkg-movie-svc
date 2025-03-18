package com.sid.moviebkg.movie.flow.theater.service;

import com.sid.moviebkg.common.dto.ResponseMsgDto;
import com.sid.moviebkg.common.model.screen.Screen;
import com.sid.moviebkg.common.model.theater.Theater;
import com.sid.moviebkg.common.utils.ValidationUtil;
import com.sid.moviebkg.movie.flow.exception.MovieFlowException;
import com.sid.moviebkg.movie.flow.theater.dto.request.TheaterRequestDto;
import com.sid.moviebkg.movie.flow.theater.repository.TheaterRepository;
import com.sid.moviebkg.movie.mapper.CmnMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
public class TheaterServiceImpl implements TheaterService {

    private final ValidationUtil validationUtil;
    private final TheaterRepository theaterRepository;

    @Transactional
    @Override
    public void saveTheater(TheaterRequestDto requestDto) {
        if (!isRequestValid(requestDto)) {
            ResponseMsgDto responseMsgDto = ResponseMsgDto.builder()
                    .exception("Mandatory fields are not present. All fields are mandatory.")
                    .build();
            throw new MovieFlowException(HttpStatus.BAD_REQUEST, responseMsgDto);
        }
        try {
            Theater theater = parseTheaterDetails(requestDto);
            theaterRepository.save(theater);
        } catch (Exception e) {
            ResponseMsgDto responseMsgDto = ResponseMsgDto.builder()
                    .exception("Internal Server Error. Failed to save Theater details.")
                    .build();
            throw new MovieFlowException(HttpStatus.INTERNAL_SERVER_ERROR, responseMsgDto);
        }
    }

    private Theater parseTheaterDetails(TheaterRequestDto requestDto) {
        LocalDateTime localDateTime = LocalDateTime.now(Clock.systemUTC());
        List<Screen> screens = requestDto.getScreens().stream()
                .map(screenDto -> Screen.builder()
                        .screenNo(screenDto.getScreenNumber())
                        .totalSeats(screenDto.getTotalSeats())
                        .build()).toList();
        return Theater.builder()
                .name(requestDto.getName())
                .address(requestDto.getAddress())
                .latitude(requestDto.getLatitude())
                .longitude(requestDto.getLongitude())
                .totalScreens(requestDto.getTotalScreens())
                .screens(screens)
                .createdDateTime(localDateTime)
                .updatedDateTime(localDateTime)
                .build();
    }

    private boolean isRequestValid(TheaterRequestDto requestDto) {
        return requestDto != null && hasText(requestDto.getName())
                && requestDto.getLatitude() != null && requestDto.getLongitude() != null && hasText(requestDto.getAddress())
                && requestDto.getTotalScreens() > 0
                && !CollectionUtils.isEmpty(requestDto.getScreens())
                && validationUtil.validateList(requestDto.getScreens(), screenDto ->
                screenDto != null && screenDto.getScreenNumber() <= 0 || screenDto != null && screenDto.getTotalSeats() <= 0);
    }
}
