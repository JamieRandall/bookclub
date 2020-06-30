package ru.club.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.club.forms.SignUpForm;
import ru.club.services.MailSender;
import ru.club.services.SignUpService;
import ru.club.transfer.UserDto;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/signup")
public class SignUpController {
    @Autowired
    private SignUpService signUpService;

    @PostMapping
    @ApiOperation(value = "Sign up")
    public ResponseEntity<Object> singUp(@RequestBody @Valid SignUpForm signUpForm) {
        Long id = signUpService.signUp(signUpForm);
        URI uri = URI.create("/users/" + id);

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/confirm/{code}")
    @ApiOperation(value = "Sign up confirmation")
    public ResponseEntity<UserDto> confirmSignUp(@PathVariable (name = "code") String code) {

        UserDto dto = signUpService.confirmSignUp(code);

        return ResponseEntity.ok(dto);
    }
}
