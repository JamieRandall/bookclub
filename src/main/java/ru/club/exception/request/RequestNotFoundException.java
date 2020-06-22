package ru.club.exception.request;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Requests not found")
public class RequestNotFoundException extends RuntimeException{
}
