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

import javax.validation.Valid;
import java.net.URI;

@RestController
public class SignUpController {
    @Autowired
    private SignUpService signUpService;

    @PostMapping("/signup")
    @ApiOperation(value = "Sign up")
    public ResponseEntity<Object> singUp(@RequestBody @Valid SignUpForm signUpForm) {
        Long id = signUpService.signUp(signUpForm);
        URI uri = URI.create("/users/" + id);

        return ResponseEntity.created(uri).build();
    }
}
