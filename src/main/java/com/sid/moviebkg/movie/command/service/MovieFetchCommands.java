package com.sid.moviebkg.movie.command.service;

import com.sid.moviebkg.common.command.dto.CommandDto;
import com.sid.moviebkg.common.command.dto.CommandRequestDto;
import com.sid.moviebkg.common.command.service.MovieCommandServiceImpl;
import com.sid.moviebkg.common.command.utils.CommandContext;
import com.sid.moviebkg.common.logging.MBkgLogger;
import com.sid.moviebkg.common.logging.MBkgLoggerFactory;
import com.sid.moviebkg.common.model.movie.Movie;
import com.sid.moviebkg.movie.command.dto.RequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieFetchCommands implements FetchCommands<Movie> {

    private MBkgLogger logger = MBkgLoggerFactory.getLogger(MovieFetchCommands.class);
    private final MovieCommandServiceImpl mvCmdSrvImpl;

    @Override
    public void generateCommands(RequestDto<Movie> requestDto) {
        if (requestDto != null && !CollectionUtils.isEmpty(requestDto.getRequestList())) {
            List<Movie> movieList = requestDto.getRequestList();
            List<CommandDto> commandDtos = CommandContext.getCommands();
            for (Movie movie : movieList) {
                CommandRequestDto<Movie> commandRequestDto = CommandRequestDto.<Movie>builder()
                        .commandKey(requestDto.getCommandKey())
                        .groupKey(requestDto.getGroupKey())
                        .requestObj(movie).initiatedBy(requestDto.getInitiatedBy())
                        .dateTime(LocalDateTime.now(Clock.systemUTC()).truncatedTo(ChronoUnit.MILLIS)).build();
                CommandDto commandDto = mvCmdSrvImpl.create(commandRequestDto, requestDto.getOperationType(), requestDto.getServiceType());
                logger.info("CommandDto:{}", commandDto);
                commandDtos.add(commandDto);
            }
        }
    }
}
