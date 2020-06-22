package ru.club.exception.handlers;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.club.exception.club.AlreadyMemberException;
import ru.club.exception.club.ClubAlreadyExistsException;
import ru.club.exception.club.ClubNotFoundException;
import ru.club.exception.club.NotMemberException;
import ru.club.exception.common.ForbiddenException;
import ru.club.exception.request.RequestAlreadyExistsException;
import ru.club.exception.request.RequestNotFoundException;
import ru.club.exception.user.UserAlreadyExistsException;
import ru.club.exception.user.UserNotFoundException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<JSONException> handleUserNotFoundException(){
        JSONException exception = new JSONException("User not found");

        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ClubNotFoundException.class)
    protected ResponseEntity<JSONException> handleClubNotFoundException() {
        JSONException exception = new JSONException("Club not found");

        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ClubAlreadyExistsException.class)
    protected ResponseEntity<JSONException> handleClubAlreadyExistsException() {
        JSONException exception = new JSONException("Club with this title already exists");

        return new ResponseEntity<>(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<JSONException> handleUserAlreadyExistsException() {
        JSONException exception = new JSONException("User with this login already exists");

        return new ResponseEntity<>(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForbiddenException.class)
    protected ResponseEntity<JSONException> handleForbiddenException() {
        JSONException exception = new JSONException("Forbidden");

        return new ResponseEntity<>(exception, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RequestNotFoundException.class)
    protected ResponseEntity<JSONException> handleRequestNotFoundException() {
        JSONException exception = new JSONException("Request not found");

        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyMemberException.class)
    protected ResponseEntity<JSONException> handleAlreadyMemberException() {
        JSONException exception = new JSONException("User is already member of this club");

        return new ResponseEntity<>(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RequestAlreadyExistsException.class)
    protected ResponseEntity<JSONException> handleRequestAlreadyExistsException() {
        JSONException exception = new JSONException("Request from this user already exists");

        return new ResponseEntity<>(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotMemberException.class)
    protected ResponseEntity<JSONException> handleNotMemberException() {
        JSONException exception = new JSONException("User is not a member");

        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    @Data
    @AllArgsConstructor
    private static class JSONException {
        private String message;
    }
}
