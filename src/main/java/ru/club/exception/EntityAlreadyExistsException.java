package ru.club.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Entity already exists")
public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException(final String message) {
        super(message);
    }
}
