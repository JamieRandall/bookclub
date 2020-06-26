package ru.club.exception.handlers;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.club.exception.EntityAlreadyExistsException;
import ru.club.exception.EntityNotFoundException;
import ru.club.exception.ForbiddenException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ForbiddenException.class)
    protected ResponseEntity<JSONException> handleForbiddenException(ForbiddenException ex) {
        JSONException exception = new JSONException(ex.getMessage());

        return new ResponseEntity<>(exception, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<JSONException> handleEntityNotFoundException(EntityNotFoundException ex) {
        JSONException exception = new JSONException(ex.getMessage());

        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    protected ResponseEntity<JSONException> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex) {
        JSONException exception = new JSONException(ex.getMessage());

        return new ResponseEntity<>(exception, HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        return handleExceptionInternal(
                ex, "VALIDATION ERROR" ,headers, HttpStatus.BAD_REQUEST, request);
    }

    @Data
    @AllArgsConstructor
    private static class JSONException {
        private String message;
    }
}
