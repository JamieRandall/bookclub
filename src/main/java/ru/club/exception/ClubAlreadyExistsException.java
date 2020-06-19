package ru.club.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Club already exists")
public class ClubAlreadyExistsException extends RuntimeException {
}
