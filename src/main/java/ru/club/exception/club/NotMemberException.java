package ru.club.exception.club;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User is not a member of the club")
public class NotMemberException extends RuntimeException {
}
