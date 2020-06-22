package ru.club.exception.club;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No such club")
public class ClubNotFoundException extends RuntimeException {
}
