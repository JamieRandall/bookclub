package ru.club.exception.club;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "User is already member of this club")
public class AlreadyMemberException extends RuntimeException {
}
