package ru.club.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Forbidden")
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(final String message) {
        super(message);
    }
}
