package com.sid.moviebkg.movie.command.service;

import com.sid.moviebkg.movie.command.dto.RequestDto;

public interface FetchCommands<T> {
    void generateCommands(RequestDto<T> requestDto);
}
