package com.sid.moviebkg.movie.command.exception;

import lombok.Getter;

@Getter
public class WebSocketPublishException extends RuntimeException {
    private final transient Exception exception;
    private final String message;

    public WebSocketPublishException(String message, Exception exception) {
        super(exception);
        this.message = message;
        this.exception = exception;
    }
}
