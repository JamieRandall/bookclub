package ru.club.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.club.forms.SignUpForm;
import ru.club.services.SignUpService;

@RestController
public class SignUpController {
    @Autowired
    private SignUpService signUpService;

    @PostMapping("/signup")
    @ApiOperation(value = "Sign up")
    public ResponseEntity<Object> singUp(@RequestBody SignUpForm signUpForm) {
        Integer status = signUpService.signUp(signUpForm);
        ResponseEntity<Object> entity;
        if (status == 409) {
            entity = ResponseEntity.status(status).body("Conflict: resource already exists");
        } else {
            entity = ResponseEntity.status(status).body("Created");
        }
        return entity;
    }
}
