package ru.club.exception.request;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Request already exists")
public class RequestAlreadyExistsException extends RuntimeException {
}
